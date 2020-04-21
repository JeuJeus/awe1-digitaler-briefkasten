package de.fhdw.geiletypengmbh.digitalerbriefkasten;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.Role;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.User;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Set;

import static java.util.Collections.emptySet;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    private static final String SITE_ROOT
            = "http://localhost:8080/";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private final String testUsername = randomAlphabetic(20);
    private final String toRegistertestUsername = randomAlphabetic(20);
    private final String testPassword = "testPassword";
    private final String testPasswordTooShort = "test";
    private static Boolean SETUPDONE = false;

    @BeforeEach
    public void prepareSetup() {
        if (!SETUPDONE) { //Workaround used here because @Before is depreceated and BeforeAll need static method
            Set<Role> emptyRoles = emptySet();
            User testUser = new User(testUsername, testPassword, testPassword, emptyRoles);
            userRepository.save(testUser);
            SETUPDONE = true;
        }
    }


    @Test
    void whenLogin_thenAuthenticated() throws Exception {
        mockMvc.perform(post(SITE_ROOT + "/login")
                .param("username", testUsername)
                .param("password", testPassword)
                .with(csrf()))
                .andExpect(authenticated());
    }

    @Test
    void whenLoginWithoutCSRF_thenUnauthenticated() throws Exception {
        mockMvc.perform(post(SITE_ROOT + "/login")
                .param("username", testUsername)
                .param("password", testPassword))
                .andExpect(unauthenticated());
    }

    @Test
    void whenLoginWithoutPassword_thenUnauthenticated() throws Exception {
        mockMvc.perform(post(SITE_ROOT + "/login")
                .param("username", testUsername)
                .with(csrf()))
                .andExpect(unauthenticated());
    }

    @Test
    void whenRegister_thenRegistered() throws Exception {
        mockMvc.perform(post(SITE_ROOT + "/registration")
                .param("username", toRegistertestUsername)
                .param("password", testPassword)
                .param("passwordConfirmation", testPassword)
                .with(csrf()))
                .andExpect(authenticated());
    }

    @Test
    void whenRegisterWithShortPassword_thenError() throws Exception {
        mockMvc.perform(post(SITE_ROOT + "/registration")
                .param("username", toRegistertestUsername)
                .param("password", testPasswordTooShort)
                .param("passwordConfirmation", testPasswordTooShort)
                .with(csrf()))
                .andExpect(unauthenticated());
    }

    @Test
    void whenLogout_ThenLoggedOut() throws Exception {
        mockMvc.perform(post(SITE_ROOT + "/login")
                .param("username", testUsername)
                .param("password", testPassword)
                .with(csrf()))
                .andExpect(authenticated());
        mockMvc.perform(post(SITE_ROOT + "/logout")
                .with(csrf()))
                .andExpect(unauthenticated());
    }

}
