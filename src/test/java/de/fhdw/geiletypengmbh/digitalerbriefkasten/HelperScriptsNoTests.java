package de.fhdw.geiletypengmbh.digitalerbriefkasten;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service.UserServiceImpl;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.Role;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.User;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.RoleRepository;
import org.junit.Ignore;
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

    @Autowired
    private RoleRepository roleRepository;

    @Ignore
    @Test
    public void createAdminIfNotExists() {
        /* Used to create Admin if not already available in production DB*/
        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        roleRepository.saveAndFlush(adminRole);
        User admin = new User("admin",
                "adminpw",
                "adminpw",
                Collections.singleton(adminRole));
        userService.save(admin);
    }

}