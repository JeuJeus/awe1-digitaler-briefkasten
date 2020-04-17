package de.fhdw.geiletypengmbh.digitalerbriefkasten;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    private static final String SITE_ROOT
            = "http://localhost:8080/";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenLogin_thenFound() throws Exception {
        mockMvc.perform(post(SITE_ROOT + "/login")
                .param("username", "nutzer123456")
                .param("password", "passwort123")
                .with(csrf()))
                .andExpect(status().isFound())
                .andReturn();
    }

    @Test
    void whenLoginWithoutCSRF_thenError() throws Exception {
        mockMvc.perform(post(SITE_ROOT + "/login")
                .param("username", "nutzer123"))
                .andExpect(status().isForbidden())
                .andReturn();
    }
}
