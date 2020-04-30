package de.fhdw.geiletypengmbh.digitalerbriefkasten.integration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service.UserServiceImpl;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions.UserNotFoundException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.Specialist;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.*;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service.*;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.*;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.User;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.Idea;
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
import org.thymeleaf.spring5.expression.Mvc;

import javax.servlet.Filter;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
//TODO BESPRECHEN, INWIEFERN WIR DIE NEUEN ENITITES TG,DC,FIELD,ADV TESTEN
public class IdeaControllerIntegTest {

    private static final String API_ROOT
            = "http://localhost:8080/api/ideas";

    private static final String TESTUSER = randomAlphabetic(10);
    private static final String TEST_SPECIALIST = randomAlphabetic(10);

    private static Boolean SETUPDONE = false;
    private static List<Advantage> advantages = new ArrayList<>();
    private static Long testFieldId;
    private static Long testDistributionChannelId;
    private static Long testTargetGroupId;
    private static Long testProductLineId;

    private static Boolean setupDone = false;

    private static final Comparator<Advantage> compareById = new Comparator<Advantage>() {
        @Override
        public int compare(Advantage o1, Advantage o2) {
            return (o1.getId() < o2.getId() ? -1 : (o1.getId() == o1.getId() ? 0 : 1));
        }
    };

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


//HELPER FUNCTIONS

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

    private static JSONObject getJsonObjectFromReturn(MvcResult mvcResult) throws UnsupportedEncodingException, JSONException {
        String jsonReturn = mvcResult.getResponse().getContentAsString();
        return new JSONObject(jsonReturn);
    }

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

    private InternalIdea createRandomInternalIdea() throws UserNotFoundException {
        InternalIdea idea = new InternalIdea();

        idea.setTitle("INTERNAL" + randomAlphabetic(10));
        idea.setDescription(randomAlphabetic(15));
        idea.setCreator(userService.findByUsername(TESTUSER));
        idea.setProductLine(productLineService.findById(testProductLineId));
        idea.setField(fieldService.findById(testFieldId));
        idea.setAdvantages(advantages);
        return idea;
    }

    private ProductIdea createRandomProductIdea() throws UserNotFoundException {
        ProductIdea idea = new ProductIdea();

        idea.setTitle("PRODUCT" + randomAlphabetic(10));
        idea.setDescription(randomAlphabetic(15));
        idea.setCreator(userService.findByUsername(TESTUSER));
        idea.setTargetGroup(targetGroupService.findById(testTargetGroupId));
        idea.setDistributionChannel(distributionChannelService.findById(testDistributionChannelId));
        idea.setAdvantages(advantages);
        idea.setProductLine(productLineService.findById(testProductLineId));
        return idea;
    }

    private String createIdeaAsUri(Idea idea) throws Exception {
        String ideaJson = parseIdeaToJson(idea);
        MvcResult mvcResult = mockMvc.perform(post(API_ROOT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(ideaJson)
                .with(user(TESTUSER))
                .with(csrf()))
                .andReturn();
        return API_ROOT + "/" + getJsonObjectFromReturn(mvcResult).get("id");
    }

    @BeforeEach
    public void prepareSetup() throws Exception {
        if (!setupDone) { //Workaround used here because @Before is depreceated and BeforeAll need static method

            mockMvc = MockMvcBuilders
                    .webAppContextSetup(context)
                    .defaultRequest(get("/").with(user(TESTUSER)))
                    .addFilters(springSecurityFilterChain)
                    .build();

            //be aware of extremly rare condition where random seed of possibility 10^26 is equal in several cases. So username reocurres and breaks test. WTF jonathan
            String tempPassword = randomAlphabetic(10);
            User testUser = new User(TESTUSER, tempPassword, tempPassword);
            userService.save(testUser);

            tempPassword = randomAlphabetic(10);
            Specialist testSpecialist = new Specialist(TEST_SPECIALIST, tempPassword, tempPassword);
            userService.save(testSpecialist);

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
            setupDone = true;
        }
    }

    @Test
    public void whenGetAllIdeas_thenOK() throws Exception {
        mockMvc.perform(get(API_ROOT))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetIdeasByTitle_thenOK() throws Exception {
        InternalIdea idea = createRandomInternalIdea();
        createIdeaAsUri(idea);

        MvcResult mvcResult = mockMvc.perform(
                get(API_ROOT + "/title/" + idea.getTitle())
                        .with(user(TESTUSER)))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertThat(response).isNotNull();
    }

    @Test
    public void whenGetCreatedInternalIdeaById_thenOK() throws Exception {
        InternalIdea internalIdea = createRandomInternalIdea();
        String location = createIdeaAsUri(internalIdea);

        MvcResult mvcResult = mockMvc.perform(
                get(location)
                        .with(user(TESTUSER)))
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

    @Test
    public void whenGetCreatedProductIdeaById_thenOK() throws Exception {
        ProductIdea productIdea = createRandomProductIdea();
        String location = createIdeaAsUri(productIdea);

        MvcResult mvcResult = mockMvc.perform(
                get(location)
                        .with(user(TESTUSER)))
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
        assertEquals(productIdea.getTargetGroup().getTitle(), persistedIdea.getTargetGroup().getTitle());
        assertEquals(productIdea.getDistributionChannel().getTitle(), productIdea.getDistributionChannel().getTitle());
    }

    @Test
    public void whenGetNotExistIdeaById_thenNotFound() throws Exception {
        mockMvc.perform(
                get(API_ROOT + "/" + randomNumeric(5))
                        .with(user(TESTUSER)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenCreateNewInternalIdea_thenCreated() throws Exception {
        Idea idea = createRandomInternalIdea();
        String ideaJson = parseIdeaToJson(idea);
        mockMvc.perform(
                post(API_ROOT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(ideaJson)
                        .with(user(TESTUSER))
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenCreateNewInternalIdeaWithSpecialist_thenCreated() throws Exception {
        Idea idea = createRandomInternalIdea();
        idea.setCreator(userService.findByUsername(TEST_SPECIALIST));
        String ideaJson = parseIdeaToJson(idea);
        mockMvc.perform(
                post(API_ROOT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(ideaJson)
                        .with(user(TESTUSER))
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenCreateNewProductIdea_thenCreated() throws Exception {
        ProductIdea idea = createRandomProductIdea();
        String ideaJson = parseIdeaToJson(idea);
        System.out.println(ideaJson);
        mockMvc.perform(
                post(API_ROOT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(ideaJson)
                        .with(user(TESTUSER))
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

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

    @Test
    public void whenInvalidInternalIdea_thenError() throws Exception {
        InternalIdea internalIdea = createRandomInternalIdea();
        internalIdea.setCreator(null);
        String ideaJson = parseIdeaToJson(internalIdea);

        mockMvc.perform(
                post(API_ROOT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(ideaJson)
                        .with(user(TESTUSER))
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenUpdateCreatedInternalIdea_thenUpdated() throws Exception {
        InternalIdea internalIdea = createRandomInternalIdea();
        String location = createIdeaAsUri(internalIdea);
        String newDescription = "new description";
        internalIdea.setId(Long.parseLong(location.split("api/ideas/")[1]));
        internalIdea.setDescription(newDescription);
        internalIdea.setCreator(userService.findByUsername(TESTUSER));
        String ideaJson = parseIdeaToJson(internalIdea);

        mockMvc.perform(
                put(location)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(ideaJson)
                        .with(user(TESTUSER))
                        .with(csrf()))
                .andExpect(status().isOk());

        MvcResult mvcResult = mockMvc.perform(
                get(location)
                        .with(user(TESTUSER))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();

        JSONObject persistedCreator = new JSONObject(getJsonObjectFromReturn(mvcResult).get("creator").toString());
        assertEquals(userService.findByUsername(TESTUSER).getUsername(), persistedCreator.get("username"));
        assertEquals(newDescription, getJsonObjectFromReturn(mvcResult).get("description"));
    }

    @Test
    public void whenInternalIdeaCreated_thenTimestampShouldBeSetCorrect() throws Exception {
        InternalIdea internalIdea = createRandomInternalIdea();
        String ideaJson = parseIdeaToJson(internalIdea);

        MvcResult mvcResult = mockMvc.perform(
                post(API_ROOT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(ideaJson)
                        .with(user(TESTUSER))
                        .with(csrf()))
                .andReturn();

        JSONObject jsonReturn = getJsonObjectFromReturn(mvcResult);
        long millis = System.currentTimeMillis();
        java.sql.Date today = new java.sql.Date(millis);

        assert (jsonReturn.get("creationDate")).equals(today.toString());
    }

    @Test
    public void whenDeleteCreatedInternalIdea_thenDeleted() throws Exception {
        InternalIdea internalIdea = createRandomInternalIdea();
        internalIdea.setStatus(Status.NOT_SUBMITTED);
        String location = createIdeaAsUri(internalIdea);

        mockMvc.perform(
                delete(location)
                        .with(user(TESTUSER).roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isOk());

        mockMvc.perform(
                get(location)
                        .with(user(TESTUSER))
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenDeleteWithoutAuthorization_thenError() throws Exception {
        Idea idea = createRandomInternalIdea();
        idea.setStatus(Status.DECLINED);
        String location = createIdeaAsUri(idea);

        mockMvc.perform(
                delete(location)
                        .with(user(TESTUSER))
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        mockMvc.perform(
                get(location)
                        .with(user(TESTUSER))
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    public void whenInternalCreated_thenDefaultStatusShouldBeSet() throws Exception {
        InternalIdea internalIdea = createRandomInternalIdea();
        String ideaJson = parseIdeaToJson(internalIdea);

        MvcResult mvcResult = mockMvc.perform(
                post(API_ROOT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(ideaJson)
                        .with(user(TESTUSER))
                        .with(csrf()))
                .andReturn();

        JSONObject jsonReturn = getJsonObjectFromReturn(mvcResult);

        assert (jsonReturn.get("status")).equals("NOT_SUBMITTED");
        //Status Justification should only be included if set
        assert (!jsonReturn.has("statusJustification"));
    }

}