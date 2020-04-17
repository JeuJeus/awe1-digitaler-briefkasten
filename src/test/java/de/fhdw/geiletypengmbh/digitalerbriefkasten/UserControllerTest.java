package de.fhdw.geiletypengmbh.digitalerbriefkasten;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    private static final String SITE_ROOT
            = "http://localhost:8080/";

    @Autowired
    private MockMvc mockMvc;

    private String testUsername = randomAlphabetic(20);
    private String testPassword = "testPassword";
    private String testPasswordTooShort = "test";


    @Test
    void whenLogin_thenAuthenticated() throws Exception {
        mockMvc.perform(post(SITE_ROOT + "/login")
                .param("username", "nutzer")
                .param("password", "passwort12")
                .with(csrf()))
                .andExpect(authenticated())
                .andReturn();
    }

    @Test
    void whenLoginWithoutCSRF_thenUnauthenticated() throws Exception {
        mockMvc.perform(post(SITE_ROOT + "/login")
                .param("username", "nutzer")
                .param("password", "passwort12"))
                .andExpect(unauthenticated())
                .andReturn();
    }

    @Test
    void whenLoginWithoutPassword_thenUnauthenticated() throws Exception {
        mockMvc.perform(post(SITE_ROOT + "/login")
                .param("username", "nutzer")
                .with(csrf()))
                .andExpect(unauthenticated())
                .andReturn();
    }

    @Test
    void whenRegister_thenRegistered() throws Exception {
        mockMvc.perform(post(SITE_ROOT + "/registration")
                .param("username", testUsername)
                .param("password", testPassword)
                .param("passwordConfirmation", testPassword)
                .with(csrf()))
                .andExpect(authenticated())
                .andReturn();
    }

    @Test
    void whenRegisterWithShortPassword_thenError() throws Exception {
        mockMvc.perform(post(SITE_ROOT + "/registration")
                .param("username", testUsername)
                .param("password", testPasswordTooShort)
                .param("passwordConfirmation", testPasswordTooShort)
                .with(csrf()))
                .andExpect(unauthenticated())
                .andReturn();
    }
}
