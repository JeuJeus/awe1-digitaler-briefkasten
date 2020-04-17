package de.fhdw.geiletypengmbh.digitalerbriefkasten;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.UserController;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.Idea;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class IdeaControllerTest {

    private static final String API_ROOT
            = "http://localhost:8080/api/ideas";

    @Autowired
    private MockMvc mockMvc;

    private Idea createRandomIdea() {
        Idea idea = new Idea();

        long millis = System.currentTimeMillis();
        java.sql.Date now = new java.sql.Date(millis);

        idea.setTitle(randomAlphabetic(10));
        idea.setDescription(randomAlphabetic(15));
        idea.setCreator(UUID.randomUUID());
        idea.setCreationDate(now);

        return idea;
    }

    private static String parseIdeaToJson(Idea idea) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(idea);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String createIdeaAsUri(Idea idea) throws Exception {
        String ideaJson = parseIdeaToJson(idea);
        MvcResult mvcResult = mockMvc.perform(post(API_ROOT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(ideaJson))
                .andReturn();

        return API_ROOT + "/" + getJsonObjectFromReturn(mvcResult).get("id");
    }

    private static JSONObject getJsonObjectFromReturn(MvcResult mvcResult) throws UnsupportedEncodingException, JSONException {
        String jsonReturn = mvcResult.getResponse().getContentAsString();
        return new JSONObject(jsonReturn);
    }

    @Before
    @Test
    public void whenLogin_thenFound() throws Exception {
        mockMvc.perform(post("http://localhost:8080/login")
                .param("username", "nutzer")
                .param("password", "passwort12")
                .with(csrf()))
                .andExpect(status().isFound())
                .andReturn();
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
                get(API_ROOT + "/title/" + idea.getTitle()))
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
                get(location))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(idea.getTitle(), getJsonObjectFromReturn(mvcResult).get("title"));
    }

    @Test
    public void whenGetNotExistIdeaById_thenNotFound() throws Exception {
        mockMvc.perform(
                get(API_ROOT + "/" + randomNumeric(4)))
                .andExpect(status().isNotFound());
    }


    @Test
    public void whenCreateNewIdea_thenCreated() throws Exception {
        Idea idea = createRandomIdea();
        String ideaJson = parseIdeaToJson(idea);

        mockMvc.perform(
                post(API_ROOT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(ideaJson))
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
                        .content(ideaJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenUpdateCreatedBook_thenUpdated() throws Exception {
        UUID randomUuid = UUID.randomUUID();
        Idea idea = createRandomIdea();
        String location = createIdeaAsUri(idea);
        idea.setId(Long.parseLong(location.split("api/ideas/")[1]));
        idea.setCreator(randomUuid);
        String ideaJson = parseIdeaToJson(idea);

        mockMvc.perform(
                put(location)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(ideaJson))
                .andExpect(status().isOk());

        MvcResult mvcResult = mockMvc.perform(
                get(location))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(randomUuid.toString(), getJsonObjectFromReturn(mvcResult).get("creator"));
    }
}

