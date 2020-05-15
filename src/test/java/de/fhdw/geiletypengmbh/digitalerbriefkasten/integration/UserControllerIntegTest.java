package de.fhdw.geiletypengmbh.digitalerbriefkasten.integration;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.Role;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.Specialist;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.User;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.ProductLine;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.service.account.UserServiceImpl;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.service.ideas.ProductLineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.Collections.emptySet;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegTest {

    private static final String SITE_ROOT
            = "http://localhost:8080/";
    private final static String TEST_USERNAME = randomAlphabetic(20);
    private final static String TO_REGISTER_USERNAME = randomAlphabetic(20);
    private final static String TEST_PASSWORD = "testPassword";
    private final static String TEST_PASSWORD_TOO_SHORT = "test";
    private static Boolean SETUPDONE = false;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ProductLineService productLineService;

    @BeforeEach
    public void prepareSetup() {
        if (!SETUPDONE) { //Workaround used here because @Before is depreceated and BeforeAll need static method
            Set<Role> emptyRoles = emptySet();
            User testUser = new User(TEST_USERNAME, TEST_PASSWORD, TEST_PASSWORD);
            userService.save(testUser);

            SETUPDONE = true;
        }
    }


    @Test
    public void whenLogin_thenAuthenticated() throws Exception {
        mockMvc.perform(post(SITE_ROOT + "/login")
                .param("username", TEST_USERNAME)
                .param("password", TEST_PASSWORD)
                .with(csrf()))
                .andExpect(authenticated());
    }

    @Test
    public void whenLoginWithoutCSRF_thenUnauthenticated() throws Exception {
        mockMvc.perform(post(SITE_ROOT + "/login")
                .param("username", TEST_USERNAME)
                .param("password", TEST_PASSWORD))
                .andExpect(unauthenticated());
    }

    @Test
    public void whenLoginWithoutPassword_thenUnauthenticated() throws Exception {
        mockMvc.perform(post(SITE_ROOT + "/login")
                .param("username", TEST_USERNAME)
                .with(csrf()))
                .andExpect(unauthenticated());
    }

    @Test
    public void whenRegister_thenRegistered() throws Exception {
        mockMvc.perform(post(SITE_ROOT + "/registration")
                .param("username", TO_REGISTER_USERNAME)
                .param("password", TEST_PASSWORD)
                .param("passwordConfirmation", TEST_PASSWORD)
                .with(csrf()))
                .andExpect(authenticated());
    }

    @Test
    public void whenRegisterWithShortPassword_thenError() throws Exception {
        mockMvc.perform(post(SITE_ROOT + "/registration")
                .param("username", TO_REGISTER_USERNAME)
                .param("password", TEST_PASSWORD_TOO_SHORT)
                .param("passwordConfirmation", TEST_PASSWORD_TOO_SHORT)
                .with(csrf()))
                .andExpect(unauthenticated());
    }

    @Test
    public void whenLogout_ThenLoggedOut() throws Exception {
        mockMvc.perform(post(SITE_ROOT + "/login")
                .param("username", TEST_USERNAME)
                .param("password", TEST_PASSWORD)
                .with(csrf()))
                .andExpect(authenticated());
        mockMvc.perform(post(SITE_ROOT + "/logout")
                .with(csrf()))
                .andExpect(unauthenticated());
    }

    @Test
    public void whenLoggedInAsUser_ThenRoleShouldBeUser() throws Exception {
        mockMvc.perform(post(SITE_ROOT + "/login")
                .param("username", TEST_USERNAME)
                .param("password", TEST_PASSWORD)
                .with(csrf()))
                .andExpect(authenticated().withRoles("USER"));
    }

    //Test Crud Method find specialist via n:m mapping
    @Test
    public void whenCreatedSpecialistFoundByProductLine_thenOkay() {
        Set<Role> emptyRoles = emptySet();
        Specialist specialist = new Specialist("testSpecialist_" + randomAlphabetic(5), TEST_PASSWORD, TEST_PASSWORD);
        Long testProductLineId = productLineService.save(
                new ProductLine(randomAlphabetic(10))
        ).getId();
        specialist.setProductLines(new ArrayList<ProductLine>() {
            {
                add(productLineService.findById(testProductLineId));
            }
        });
        userService.save(specialist);
        List<Specialist> matchingSpecialists = userService.findSpecialistByProductLine_id(testProductLineId);
        assertThat(specialist.getId() != 0); // means that specialist has been persisted
        assertThat(matchingSpecialists.get(0).getId() == specialist.getId());
    }
}
