package de.fhdw.geiletypengmbh.digitalerbriefkasten;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.Idea;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
/*
    @Test
    public void whenGetNotExistIdeaById_thenNotFound() {
        Response response = RestAssured.get(API_ROOT + "/" + randomNumeric(4));

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }


    @Test
    public void whenCreateNewIdea_thenCreated() {
        Idea idea = createRandomIdea();
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(idea)
                .post(API_ROOT);

        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
    }


    @Test
    public void whenInvalidIdea_thenError() {
        Idea idea = createRandomIdea();
        idea.setCreator(null);
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(idea)
                .post(API_ROOT);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }

    @Test
    public void whenUpdateCreatedBook_thenUpdated() {
        UUID randomUuid = UUID.randomUUID();
        Idea idea = createRandomIdea();
        String location = createIdeaAsUri(idea);
        idea.setId(Long.parseLong(location.split("api/ideas/")[1]));
        idea.setCreator(randomUuid);
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(idea)
                .put(location);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        response = RestAssured.get(location);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(randomUuid.toString(), response.jsonPath()
                .get("creator"));
    }*/
}

