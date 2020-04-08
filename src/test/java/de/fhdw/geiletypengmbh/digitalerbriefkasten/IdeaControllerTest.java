package de.fhdw.geiletypengmbh.digitalerbriefkasten;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.IdeaController;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.Idea;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.IdeaRepository;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.config.http.MatcherType.mvc;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
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

    private String createIdeaAsUri(Idea idea) {
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(idea)
                .post(API_ROOT);
        return API_ROOT + "/" + response.jsonPath().get("id");
    }

    @Test
    public void whenGetAllIdeas_thenOK() throws Exception {
        mockMvc.perform(get(API_ROOT))
                .andDo(print())
                .andExpect(status().isOk());
    }

    /*
    @Test
    public void whenGetIdeasByTitle_thenOK() {
        Idea idea = createRandomIdea();
        createIdeaAsUri(idea);
        Response response = RestAssured.get(
                API_ROOT + "/title/" + idea.getTitle());

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertTrue(response.as(List.class)
                .size() > 0);
    }

    @Test
    public void whenGetCreatedIdeaById_thenOK() {
        Idea idea = createRandomIdea();
        String location = createIdeaAsUri(idea);
        Response response = RestAssured.get(location);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(idea.getTitle(), response.jsonPath()
                .get("title"));
    }

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

