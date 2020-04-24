package de.fhdw.geiletypengmbh.digitalerbriefkasten;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service.UserServiceImpl;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.Role;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HelperScriptsNoTests {

    @Autowired
    private UserServiceImpl userService;

    @Test
    public void createAdminIfNotExists() {
        /* Used to create Admin in production DB*/
        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        User admin = new User("admin",
                "adminpw",
                "adminpw",
                Collections.singleton(adminRole));
        userService.save(admin);
    }

}
