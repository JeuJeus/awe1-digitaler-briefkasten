package de.fhdw.geiletypengmbh.digitalerbriefkasten;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.Specialist;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.service.account.UserServiceImpl;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.Role;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.User;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.account.RoleRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
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
        //TODO MAKE ME AVAILABLE FOR DEPLOYMENT!!!
        /* Used to create Admin if not already available in production DB*/
        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        roleRepository.saveAndFlush(adminRole);
        User admin = new User("admin",
                "hierKönnteIhreWerbungStehen",
                "hierKönnteIhreWerbungStehen");
        admin.setRoles(Collections.singleton(adminRole));
        userService.save(admin);
    }

    @Ignore
    @Test
    public void createSpecialistIfNotExists() {
        //TODO REMOVE ME
        /* Used to test Specialist while not implemented to be created otherwise*/
        Role specialistRole = new Role();
        specialistRole.setName("SPECIALIST");
        roleRepository.saveAndFlush(specialistRole);
        Specialist specialist = new Specialist("specialist",
                "specialistpw",
                "specialistpw");
        specialist.setRoles(Collections.singleton(specialistRole));
        userService.save(specialist);
    }
}
