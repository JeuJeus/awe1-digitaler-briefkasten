package de.fhdw.geiletypengmbh.digitalerbriefkasten.integration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service.UserServiceImpl;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.Idea;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.Role;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.User;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.InternalIdea;
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
import java.util.Set;

import static java.util.Collections.emptySet;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class IdeaControllerIntegTest {

    private static final String API_ROOT
            = "http://localhost:8080/api/ideas";

    private static final String TESTUSER = randomAlphabetic(10);

    private static Boolean SETUPDONE = false;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private UserServiceImpl userService;

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

    private Idea createRandomIdea() {
        Idea idea = new Idea();

        idea.setTitle(randomAlphabetic(10));
        idea.setDescription(randomAlphabetic(15));
        idea.setCreator(userService.findByUsername(TESTUSER));

        return idea;
    }

    private Idea createRandomInternalIdea() {
        InternalIdea idea = new InternalIdea();

        idea.setTitle("INTERNAL" + randomAlphabetic(10));
        idea.setDescription(randomAlphabetic(15));
        idea.setCreator(userService.findByUsername(TESTUSER));
        idea.setField("INTERNAL FIELD");
        return idea;
    }

    private String createIdeaAsUri(Idea idea) throws Exception {
        String ideaJson = parseIdeaToJson(idea);
        MvcResult mvcResult = mockMvc.perform(post(API_ROOT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(ideaJson)
                .with(csrf()))
                .andReturn();

        return API_ROOT + "/" + getJsonObjectFromReturn(mvcResult).get("id");
    }

    @BeforeEach
    public void prepareSetup() {
        if (!SETUPDONE) { //Workaround used here because @Before is depreceated and BeforeAll need static method

            mockMvc = MockMvcBuilders
                    .webAppContextSetup(context)
                    .defaultRequest(get("/").with(user("user").roles("ADMIN")))
                    .addFilters(springSecurityFilterChain)
                    .build();

            //be aware of extremly rare condition where random seed of possibility 10^26 is equal in several cases. So username reocurres and breaks test. WTF jonathan
            String tempPassword = randomAlphabetic(10);
            Set<Role> emptyRoles = emptySet();
            User testUser = new User(TESTUSER, tempPassword, tempPassword, emptyRoles);
            userService.save(testUser);

            SETUPDONE = true;
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
        Idea idea = createRandomIdea();
        createIdeaAsUri(idea);

        MvcResult mvcResult = mockMvc.perform(
                get(API_ROOT + "/title/" + idea.getTitle())
                        .with(user("user")))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertThat(response).isNotNull();
    }

    @Test
    public void whenGetCreatedIdeaById_thenOK() throws Exception {
        Idea idea = createRandomIdea();
        String location = createIdeaAsUri(idea);

        MvcResult mvcResult = mockMvc.perform(
                get(location)
                        .with(user("user")))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(idea.getTitle(), getJsonObjectFromReturn(mvcResult).get("title"));
    }

    @Test
    public void whenGetNotExistIdeaById_thenNotFound() throws Exception {
        mockMvc.perform(
                get(API_ROOT + "/" + randomNumeric(5))
                        .with(user("user")))
                .andExpect(status().isNotFound());
    }


    @Test
    public void whenCreateNewIdea_thenCreated() throws Exception {
        Idea idea = createRandomIdea();
        String ideaJson = parseIdeaToJson(idea);

        mockMvc.perform(
                post(API_ROOT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(ideaJson)
                        .with(user("user"))
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenCreateNewInternalIdea_thenCreated() throws Exception {
        Idea idea = createRandomInternalIdea();
        String ideaJson = parseIdeaToJson(idea);

        mockMvc.perform(
                post(API_ROOT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(ideaJson)
                        .with(user("test"))
                        .with(csrf()))
                .andExpect(status().isCreated());
    }


    @Test
    public void whenInvalidIdea_thenError() throws Exception {
        Idea idea = createRandomIdea();
        idea.setCreator(null);
        String ideaJson = parseIdeaToJson(idea);

        mockMvc.perform(
                post(API_ROOT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(ideaJson)
                        .with(user("user"))
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenUpdateCreatedIdea_thenUpdated() throws Exception {
        Idea idea = createRandomIdea();
        String location = createIdeaAsUri(idea);
        idea.setId(Long.parseLong(location.split("api/ideas/")[1]));
        idea.setCreator(userService.findByUsername(TESTUSER));
        String ideaJson = parseIdeaToJson(idea);

        mockMvc.perform(
                put(location)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(ideaJson)
                        .with(user("user"))
                        .with(csrf()))
                .andExpect(status().isOk());

        MvcResult mvcResult = mockMvc.perform(
                get(location)
                        .with(user("user"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();

        JSONObject persistedCreator = new JSONObject(getJsonObjectFromReturn(mvcResult).get("creator").toString());
        assertEquals(userService.findByUsername(TESTUSER).getUsername(), persistedCreator.get("username"));
    }

    @Test
    public void whenIdeaCreated_thenTimestampShouldBeSetCorrect() throws Exception {
        Idea idea = createRandomIdea();
        String ideaJson = parseIdeaToJson(idea);

        MvcResult mvcResult = mockMvc.perform(
                post(API_ROOT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(ideaJson)
                        .with(user("user"))
                        .with(csrf()))
                .andReturn();

        JSONObject jsonReturn = getJsonObjectFromReturn(mvcResult);
        long millis = System.currentTimeMillis();
        java.sql.Date today = new java.sql.Date(millis);

        assert (jsonReturn.get("creationDate")).equals(today.toString());
    }

    @Test
    public void whenDeleteCreatedIdea_thenDeleted() throws Exception {
        Idea idea = createRandomIdea();
        String location = createIdeaAsUri(idea);

        mockMvc.perform(
                delete(location)
                        .with(user("user"))
                        .with(csrf()))
                .andExpect(status().isOk());

        mockMvc.perform(
                get(location)
                        .with(user("user"))
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenCreated_thenDefaultStatusShouldBeSet() throws Exception {
        Idea idea = createRandomIdea();
        String ideaJson = parseIdeaToJson(idea);

        MvcResult mvcResult = mockMvc.perform(
                post(API_ROOT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(ideaJson)
                        .with(user("user"))
                        .with(csrf()))
                .andReturn();

        JSONObject jsonReturn = getJsonObjectFromReturn(mvcResult);

        assert (jsonReturn.get("status")).equals("NOT_SUBMITTED");
        //Status Justification should only be included if set
        assert (!jsonReturn.has("statusJustification"));
    }
}

