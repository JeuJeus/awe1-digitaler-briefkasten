package de.fhdw.geiletypengmbh.digitalerbriefkasten;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.Role;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.Specialist;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.User;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.DistributionChannel;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.Field;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.ProductLine;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.TargetGroup;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.account.RoleRepository;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.ideas.DistributionChannelRepository;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.ideas.FieldRepository;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.ideas.ProductLineRepository;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.ideas.TargetGroupRepository;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.service.account.UserServiceImpl;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.service.ideas.IdeaService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HelperScriptsNoTests {

    private final static String[] productLines = {"KFZ", "Unfall", "Krankenversicherung", "Rechtsschutz", "Lebensversicherung",
            "Rentenversicherung", "Haftpflicht", "Hausrat", "Wohngebäudeversicherung"};
    private final static String[] distributionChannels = {"Stationärer Vertrieb", "Versicherungsmakler",
            "Kooperation mit Kreditinstituten", "Direktversicherung"};
    private final static String[] targetGroups = {"Kinder / Jugendliche", "Singles", "Paare", "Personen 50+", "Gewerbetreibende"};
    private final static String[] fields = {"Kostensenkung", "Ertragssteigerung", "Zukunftsfähigkeit"};
    @Autowired
    IdeaService ideaService;
    @Autowired
    ProductLineRepository productLineRepository;
    @Autowired
    DistributionChannelRepository distributionChannelRepository;
    @Autowired
    TargetGroupRepository targetGroupRepository;
    @Autowired
    FieldRepository fieldRepository;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private RoleRepository roleRepository;

    //Autor: JF
    @Ignore
    @Test
    public void createAdminIfNotExists() {
        /* Used to create Admin if not already available in production DB*/
        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        roleRepository.saveAndFlush(adminRole);
        User admin = new User("admin",
                "hierKönnteIhreWerbungStehen",
                "hierKönnteIhreWerbungStehen",
                "Sys",
                "Admin");
        admin.setRoles(Collections.singleton(adminRole));
        userService.save(admin);
    }

    //Autor: PR
    @Ignore
    @Test
    public void createAPIUserIfNotExists() {
        /* Used to create API User if not already available in production DB*/
        Role role = new Role();
        role.setName("API_USER");
        roleRepository.saveAndFlush(role);
        User user = new User("API_USER",
                "API-usus-maximus-passwortus-securitus",
                "API-usus-maximus-passwortus-securitus",
                "API",
                "USER");
        user.setRoles(Collections.singleton(role));
        userService.save(user);
    }

    //Autor: PR
    @Ignore
    @Test
    public void createDefaultDatabaseEntrys() {
        if (productLineRepository.findByTitle(ideaService.getDefaultInternalProductLineTitle()) == null) {
            productLineRepository.save(new ProductLine(ideaService.getDefaultInternalProductLineTitle()));
        }
        Stream.concat(Arrays.stream(productLines), Stream.of(ideaService.getDefaultInternalProductLineTitle())).forEach(productLine -> {
            if (productLineRepository.findByTitle(productLine) == null) {
                productLineRepository.save(new ProductLine(productLine));
            }
        });

        Arrays.stream(distributionChannels).forEach(distributionChannel -> {
            if (distributionChannelRepository.findByTitle(distributionChannel) == null) {
                distributionChannelRepository.save(new DistributionChannel(distributionChannel));
            }
        });

        Arrays.stream(targetGroups).forEach(targetGroup -> {
            if (targetGroupRepository.findByTitle(targetGroup) == null) {
                targetGroupRepository.save(new TargetGroup(targetGroup));
            }
        });

        Arrays.stream(fields).forEach(field -> {
            if (fieldRepository.findByTitle(field) == null) {
                fieldRepository.save(new Field(field));
            }
        });
    }

    //Autor: PR
    @Ignore
    @Test
    public void createTestSpecialists() {
        createDefaultDatabaseEntrys();
        AtomicInteger i = new AtomicInteger(2000);
        Stream.concat(Arrays.stream(productLines), Stream.of(ideaService.getDefaultInternalProductLineTitle())).forEach(productLine -> {
            ProductLine pLine = productLineRepository.findByTitle(productLine);
            if (pLine != null && userService.findSpecialistByproductLineId(pLine.getId()).size() == 0) {
                Specialist specialist = new Specialist("SpeziusMaximus_" + pLine.getTitle(), "boringProphet",
                        "boringProphet", "Spezius", "Maximus");
                specialist.setProductLines(new ArrayList<>() {
                    {
                        add(pLine);
                    }
                });
                userService.save(specialist);
                i.getAndIncrement();
            }
        });
    }
}
