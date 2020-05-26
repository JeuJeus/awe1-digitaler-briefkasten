package de.fhdw.geiletypengmbh.digitalerbriefkasten.integration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.UserNotFoundException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.Role;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.Specialist;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.User;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.*;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.service.account.RoleService;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.service.account.UserServiceImpl;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.service.ideas.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class IdeaControllerIntegTest {
        //TODO FIX TESTS REGARDING HTTP STATUS PRODUCED BY EXCEPTION HANDLER
    private static final String API_ROOT
            = "http://localhost:8080/api/ideas";

    private static final String TEST_USER = randomAlphabetic(10);
    private static final String TEST_SPECIALIST = randomAlphabetic(10);
    private static final Comparator<Advantage> compareById = new Comparator<Advantage>() {
        @Override
        public int compare(Advantage o1, Advantage o2) {
            return (o1.getId() < o2.getId() ? -1 : (o1.getId() == o1.getId() ? 0 : 1));
        }
    };
    private static Boolean setupDone = false;
    private static List<Advantage> advantages = new ArrayList<>();
    private static Long testFieldId;
    private static Long testDistributionChannelId;
    private static Long testTargetGroupId;
    private static Long testProductLineId;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AdvantageService advantageService;

    @Autowired
    private TargetGroupService targetGroupService;

    @Autowired
    private DistributionChannelService distributionChannelService;

    @Autowired
    private FieldService fieldService;

    @Autowired
    private ProductLineService productLineService;

    @Autowired
    private IdeaService ideaService;

    @Autowired
    private RoleService roleService;


//HELPER FUNCTIONS

    //Autor: JF
    private static String parseIdeaToJson(Idea idea) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        try {
            return mapper.writeValueAsString(idea);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Autor: JF
    private static JSONObject getJsonObjectFromReturn(MvcResult mvcResult) throws UnsupportedEncodingException, JSONException {
        String jsonReturn = mvcResult.getResponse().getContentAsString();
        return new JSONObject(jsonReturn);
    }

    //Autor: PR
    private static InternalIdea getInternalIdeaFromReturn(MvcResult mvcResult) {
        InternalIdea idea = new InternalIdea();
        try {
            JSONObject persistedIdea = getJsonObjectFromReturn(mvcResult);
            idea = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), InternalIdea.class);
        } catch (UnsupportedEncodingException | JSONException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return idea;
    }

    //Autor: PR
    private static ProductIdea getProductIdeaFromReturn(MvcResult mvcResult) {
        ProductIdea idea = new ProductIdea();
        try {
            JSONObject persistedIdea = getJsonObjectFromReturn(mvcResult);
            idea = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), ProductIdea.class);
        } catch (UnsupportedEncodingException | JSONException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return idea;
    }

    //Autor: JF
    private InternalIdea createRandomInternalIdea() throws UserNotFoundException {
        InternalIdea idea = new InternalIdea();

        idea.setTitle("INTERNAL" + randomAlphabetic(10));
        idea.setDescription(randomAlphabetic(15));
        idea.setCreator(userService.findByUsername(TEST_USER));
        idea.setProductLine(productLineService.findById(testProductLineId));
        idea.setField(fieldService.findById(testFieldId));
        idea.setAdvantages(advantages);
        return idea;
    }

    //Autor: PR
    private ProductIdea createRandomProductIdea() throws UserNotFoundException {
        ProductIdea idea = new ProductIdea();

        idea.setTitle("PRODUCT" + randomAlphabetic(10));
        idea.setDescription(randomAlphabetic(15));
        idea.setCreator(userService.findByUsername(TEST_USER));
        idea.setTargetGroups(new HashSet<TargetGroup>() {
            {
                add(targetGroupService.findById(testTargetGroupId));
            }
        });
        idea.setDistributionChannels(new HashSet<DistributionChannel>() {
            {
                add(distributionChannelService.findById(testDistributionChannelId));
            }
        });
        idea.setAdvantages(advantages);
        idea.setProductLine(productLineService.findById(testProductLineId));
        return idea;
    }

    //Autor: JF
    private String createIdeaAsUri(Idea idea) throws Exception {
        String ideaJson = parseIdeaToJson(idea);

        MvcResult mvcResult = mockMvc.perform(post(API_ROOT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(ideaJson)
                .with(user(TEST_USER).roles("API_USER"))
                .with(csrf()))
                .andReturn();
        return API_ROOT + "/" + getJsonObjectFromReturn(mvcResult).get("id");
    }

    //Autor: PR
    @BeforeEach
    public void prepareSetup() throws Exception {
        if (!setupDone) { //Workaround used here because @Before is depreceated and BeforeAll need static method

            mockMvc = MockMvcBuilders
                    .webAppContextSetup(context)
                    .defaultRequest(get("/").with(user(TEST_USER).roles("API_USER")))
                    .addFilters(springSecurityFilterChain)
                    .build();

            Role apiRole = new Role("API_USER");
            roleService.save(apiRole);
            //be aware of extremly rare condition where random seed of possibility 10^26 is equal in several cases. So username reocurres and breaks test. WTF jonathan
            String tempPassword = randomAlphabetic(10);
            User testUser = new User(IdeaControllerIntegTest.TEST_USER, tempPassword, tempPassword);
            testUser.setRoles(Collections.singleton(apiRole));
            userService.save(testUser);

            testFieldId = fieldService.save(
                    new Field(randomAlphabetic(10))
            ).getId();
            testTargetGroupId = targetGroupService.save(
                    new TargetGroup(randomAlphabetic(10))
            ).getId();
            testDistributionChannelId = distributionChannelService.save(
                    new DistributionChannel(randomAlphabetic(10))
            ).getId();
            testProductLineId = productLineService.save(
                    new ProductLine(randomAlphabetic(10))
            ).getId();
            advantages.add(new Advantage(randomAlphabetic(10)));
            advantages.add(new Advantage(randomAlphabetic(10)));
            advantages.add(new Advantage(randomAlphabetic(10)));

            tempPassword = randomAlphabetic(10);
            Specialist testSpecialist = new Specialist(TEST_SPECIALIST, tempPassword, tempPassword);
            testSpecialist.setProductLines(new ArrayList<ProductLine>() {
                {
                    add(productLineService.findById(testProductLineId));
                }
            });
            userService.save(testSpecialist);

            setupDone = true;
        }
    }

    //Autor: JF
    @Test
    public void whenGetAllIdeas_thenOK() throws Exception {
        mockMvc.perform(get(API_ROOT)
                .with(user(TEST_USER).roles("API_USER")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    //Autor: JF
    @Test
    public void whenGetIdeasByTitle_thenOK() throws Exception {
        InternalIdea idea = createRandomInternalIdea();
        createIdeaAsUri(idea);

        MvcResult mvcResult = mockMvc.perform(
                get(API_ROOT + "/title/" + idea.getTitle())
                        .with(user(TEST_USER).roles("API_USER")))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertThat(response).isNotNull();
    }

    //Autor: PR
    @Test
    public void whenGetCreatedInternalIdeaById_thenOK() throws Exception {
        InternalIdea internalIdea = createRandomInternalIdea();
        String location = createIdeaAsUri(internalIdea);

        MvcResult mvcResult = mockMvc.perform(
                get(location)
                        .with(user(TEST_USER).roles("API_USER")))
                .andExpect(status().isOk())
                .andReturn();

        InternalIdea persistedIdea = getInternalIdeaFromReturn(mvcResult);
        int advSize = Math.max(internalIdea.getAdvantages().size(), persistedIdea.getAdvantages().size());
        Collections.sort(internalIdea.getAdvantages(), compareById);
        Collections.sort(persistedIdea.getAdvantages(), compareById);
        for (int i = 0; i < advSize; i++) {
            assertEquals(internalIdea.getAdvantages().get(i).getDescription(), persistedIdea.getAdvantages().get(i).getDescription());
        }
        assertEquals(internalIdea.getProductLine().getTitle(), persistedIdea.getProductLine().getTitle());
        assertEquals(internalIdea.getField().getTitle(), persistedIdea.getField().getTitle());
    }

    //Autor: PR
    @Test
    public void whenGetCreatedProductIdeaById_thenOK() throws Exception {
        ProductIdea productIdea = createRandomProductIdea();
        String location = createIdeaAsUri(productIdea);

        MvcResult mvcResult = mockMvc.perform(
                get(location)
                        .with(user(TEST_USER).roles("API_USER")))
                .andExpect(status().isOk())
                .andReturn();

        ProductIdea persistedIdea = getProductIdeaFromReturn(mvcResult);
        int advSize = Math.max(productIdea.getAdvantages().size(), persistedIdea.getAdvantages().size());
        Collections.sort(productIdea.getAdvantages(), compareById);
        Collections.sort(persistedIdea.getAdvantages(), compareById);
        for (int i = 0; i < advSize; i++) {
            assertEquals(productIdea.getAdvantages().get(i).getDescription(), persistedIdea.getAdvantages().get(i).getDescription());
        }
        assertEquals(productIdea.getProductLine().getTitle(), persistedIdea.getProductLine().getTitle());
        assertEquals(productIdea.getTargetGroups().stream().findFirst().get().getTitle(),
                persistedIdea.getTargetGroups().stream().findFirst().get().getTitle());
        assertEquals(productIdea.getDistributionChannels().stream().findFirst().get().getTitle(),
                productIdea.getDistributionChannels().stream().findFirst().get().getTitle());
    }

    //Autor: JF
    @Test
    public void whenGetNotExistIdeaById_thenNotFound() throws Exception {
        mockMvc.perform(
                get(API_ROOT + "/" + randomNumeric(5))
                        .with(user(TEST_USER).roles("API_USER")))
                .andExpect(status().isNotFound());
    }

    //Autor: JF
    @Test
    public void whenCreateNewInternalIdea_thenCreated() throws Exception {
        Idea idea = createRandomInternalIdea();
        String ideaJson = parseIdeaToJson(idea);
        mockMvc.perform(
                post(API_ROOT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(ideaJson)
                        .with(user(TEST_USER).roles("API_USER"))
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

    //Autor: PR
    @Test
    public void whenCreateNewInternalIdeaWithSpecialist_thenCreated() throws Exception {
        Idea idea = createRandomInternalIdea();
        idea.setCreator(userService.findByUsername(TEST_SPECIALIST));
        idea.setSpecialist((Specialist) idea.getCreator());
        String ideaJson = parseIdeaToJson(idea);
        mockMvc.perform(
                post(API_ROOT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(ideaJson)
                        .with(user(TEST_USER).roles("API_USER"))
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

    //Autor: JS
    @Test
    public void whenCreateNewProductIdea_thenCreated() throws Exception {
        ProductIdea idea = createRandomProductIdea();
        String ideaJson = parseIdeaToJson(idea);
        mockMvc.perform(
                post(API_ROOT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(ideaJson)
                        .with(user(TEST_USER).roles("API_USER"))
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

    //Autor: PR
    @Test
    public void whenCreateNewIdeaNotLoggedIn_thenNotAuthorized() throws Exception {
        InternalIdea idea = createRandomInternalIdea();
        String ideaJson = parseIdeaToJson(idea);

        mockMvc.perform(
                post(API_ROOT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(ideaJson))
                .andExpect(status().isForbidden());
    }

    //Autor: JF
    @Test
    public void whenInvalidInternalIdea_thenError() throws Exception {
        InternalIdea internalIdea = createRandomInternalIdea();
        internalIdea.setCreator(null);
        String ideaJson = parseIdeaToJson(internalIdea);

        mockMvc.perform(
                post(API_ROOT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(ideaJson)
                        .with(user(TEST_USER).roles("API_USER"))
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    //Autor: JF
    @Test
    public void whenUpdateCreatedInternalIdea_thenUpdated() throws Exception {
        InternalIdea internalIdea = createRandomInternalIdea();
        String location = createIdeaAsUri(internalIdea);
        String newDescription = "new description";
        internalIdea.setId(Long.parseLong(location.split("api/ideas/")[1]));
        internalIdea.setDescription(newDescription);
        internalIdea.setCreator(userService.findByUsername(TEST_USER));
        String ideaJson = parseIdeaToJson(internalIdea);

        mockMvc.perform(
                put(location)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(ideaJson)
                        .with(user(TEST_USER).roles("API_USER"))
                        .with(csrf()))
                .andExpect(status().isOk());

        MvcResult mvcResult = mockMvc.perform(
                get(location)
                        .with(user(TEST_USER).roles("API_USER"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();

        JSONObject persistedCreator = new JSONObject(getJsonObjectFromReturn(mvcResult).get("creator").toString());
        assertEquals(userService.findByUsername(TEST_USER).getUsername(), persistedCreator.get("username"));
        assertEquals(newDescription, getJsonObjectFromReturn(mvcResult).get("description"));
    }

    //Autor: JF
    @Test
    public void whenInternalIdeaCreated_thenTimestampShouldBeSetCorrect() throws Exception {
        InternalIdea internalIdea = createRandomInternalIdea();
        String ideaJson = parseIdeaToJson(internalIdea);

        MvcResult mvcResult = mockMvc.perform(
                post(API_ROOT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(ideaJson)
                        .with(user(TEST_USER).roles("API_USER"))
                        .with(csrf()))
                .andReturn();

        JSONObject jsonReturn = getJsonObjectFromReturn(mvcResult);
        long millis = System.currentTimeMillis();
        java.sql.Date today = new java.sql.Date(millis);

        assertEquals (jsonReturn.get("creationDate"),today.toString());
    }

    //Autor: JF
    @Test
    public void whenDeleteCreatedInternalIdea_thenDeleted() throws Exception {
        InternalIdea internalIdea = createRandomInternalIdea();
        internalIdea.setStatus(Status.NOT_SUBMITTED);
        String location = createIdeaAsUri(internalIdea);

        mockMvc.perform(
                delete(location)
                        .with(user(TEST_USER).roles("API_USER"))
                        .with(csrf()))
                .andExpect(status().isOk());

        mockMvc.perform(
                get(location)
                        .with(user(TEST_USER).roles("API_USER"))
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    //Autor: JF
    @Test
    public void whenDeleteWithoutAuthorization_thenError() throws Exception {
        Idea idea = createRandomInternalIdea();
        idea.setStatus(Status.DECLINED);
        String location = createIdeaAsUri(idea);

        mockMvc.perform(
                delete(location)
                        .with(user(TEST_USER).roles("API_USER"))
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        mockMvc.perform(
                get(location)
                        .with(user(TEST_USER).roles("API_USER"))
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    //Autor: JF
    @Test
    public void whenInternalCreated_thenDefaultStatusShouldBeSet() throws Exception {
        InternalIdea internalIdea = createRandomInternalIdea();
        String ideaJson = parseIdeaToJson(internalIdea);

        MvcResult mvcResult = mockMvc.perform(
                post(API_ROOT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(ideaJson)
                        .with(user(TEST_USER).roles("API_USER"))
                        .with(csrf()))
                .andReturn();

        JSONObject jsonReturn = getJsonObjectFromReturn(mvcResult);

        assertEquals(jsonReturn.get("status"),"NOT_SUBMITTED");
        //Status Justification should only be included if set
        assertFalse(jsonReturn.has("statusJustification"));
    }

    //Autor: PR
    @Test
    public void whenGetTestUserAndSpecialist_thenOkay() throws UserNotFoundException {
        //Created in setup method before
        assertEquals(TEST_USER, userService.findByUsername(TEST_USER).getUsername());
        assertEquals(TEST_SPECIALIST, userService.findByUsername(TEST_SPECIALIST).getUsername());
        assertEquals(User.class, userService.findByUsername(TEST_USER).getClass());
        assertEquals(Specialist.class, userService.findByUsername(TEST_SPECIALIST).getClass());
        Long persistedTestProductLineId = ((Specialist) userService.findByUsername(TEST_SPECIALIST)).getProductLines().get(0).getId();
        assertEquals(testProductLineId, persistedTestProductLineId);
    }

    //Autor: PR
    @Test
    public void whenGetSpecialistOfNewInternalIdea_ThenOkay() throws Exception {
        final String TEST_PASSWORD = randomAlphabetic(10);
        Specialist specialist = new Specialist("testSpecialist_" + randomAlphabetic(5), TEST_PASSWORD, TEST_PASSWORD);
        Long internalProductLineId = productLineService.save(
                new ProductLine(ideaService.getDefaultInternalProductLineTitle())
        ).getId();
        specialist.setProductLines(new ArrayList<ProductLine>() {
            {
                add(productLineService.findById(internalProductLineId));
            }
        });
        userService.save(specialist);
        InternalIdea idea = createRandomInternalIdea();
        idea.setProductLine(productLineService.findById(internalProductLineId));
        createIdeaAsUri(idea);
        Specialist internalSpecialist = ideaService.getSpecialistOfNewIdea(idea);
        assertEquals(internalSpecialist.getId(), specialist.getId());
    }

    //Autor: PR
    @Test
    public void whenGetSpecialistOfNewProductIdea_ThenOkay() throws Exception {
        final String TEST_PASSWORD = randomAlphabetic(10);
        Specialist specialist = new Specialist("testSpecialist_" + randomAlphabetic(5), TEST_PASSWORD, TEST_PASSWORD);
        Long productLineId = productLineService.save(
                new ProductLine(randomAlphabetic(10))
        ).getId();
        specialist.setProductLines(new ArrayList<ProductLine>() {
            {
                add(productLineService.findById(productLineId));
            }
        });
        userService.save(specialist);
        ProductIdea idea = createRandomProductIdea();
        idea.setProductLine(productLineService.findById(productLineId));
        createIdeaAsUri(idea);
        Specialist productSpecialist = ideaService.getSpecialistOfNewIdea(idea);
        assertEquals(productSpecialist.getId(), specialist.getId());
    }

    //Autor: PR
    @Test
    public void whenGetSpecialistWithLeastSubmittedIdeasForNewProductIdea_ThenOkay() throws Exception {
        final String TEST_PASSWORD = randomAlphabetic(10);
        ArrayList<Specialist> specialists = new ArrayList<>();
        // Create test productline
        Long productLineId = productLineService.save(
                new ProductLine(randomAlphabetic(10))
        ).getId();
        // Create 3 specialists
        for (int i = 0; i < 3; i++) {
            specialists.add(new Specialist("testSpecialist_" + randomAlphabetic(5), TEST_PASSWORD, TEST_PASSWORD));
            specialists.get(i).setProductLines(new ArrayList<ProductLine>() {
                {
                    add(productLineService.findById(productLineId));
                }
            });
            userService.save(specialists.get(i));
        }
        // Create 6 ideas that get matched to the 3 specialists
        // 1st specialist 2 ideas
        // 2nd specialist 1 idea  -> this one should get returned by ideaService.getSpecialistOfNewIdea()
        // 3rd specialist 3 ideas
        for (int i = 0; i < 6; i++) {
            ProductIdea idea = createRandomProductIdea();
            idea.setProductLine(productLineService.findById(productLineId));
            if (i <= 1) idea.setSpecialist(specialists.get(0));
            else if (i == 2) idea.setSpecialist(specialists.get(1));
            else idea.setSpecialist(specialists.get(2));
            createIdeaAsUri(idea);
        }
        // New Idea for testing assignment
        ProductIdea idea = createRandomProductIdea();
        idea.setProductLine(productLineService.findById(productLineId));

        Specialist productSpecialist = ideaService.getSpecialistOfNewIdea(idea);
        assertEquals(productSpecialist.getId(), specialists.get(1).getId());
    }
}