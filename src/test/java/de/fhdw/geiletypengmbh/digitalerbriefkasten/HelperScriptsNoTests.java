package de.fhdw.geiletypengmbh.digitalerbriefkasten;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.Role;
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
import de.fhdw.geiletypengmbh.digitalerbriefkasten.service.ideas.*;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HelperScriptsNoTests {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RoleRepository roleRepository;

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

    @Ignore
    @Test
    public void createDefaultDatabaseEntrys() {
        String[] productLines = {"KFZ", "Unfall", "Krankenversicherung", "Rechtsschutz", "Lebensversicherung",
                "Rentenversicherung", "Haftpflicht", "Hausrat", "Wohngebäudeversicherung"};

        String[] distributionChannels = {"Stationärer Vertrieb", "Versicherungsmakler",
                "Kooperation mit Kreditinstituten", "Direktversicherung"};

        String[] targetGroups = {"Kinder / Jugendliche", "Singles", "Paare", "Personen 50+", "Gewerbetreibende"};

        String[] fields = {"Kostensenkung", "Ertragssteigerung", "Zukunftsfähigkeit"};

        if (productLineRepository.findByTitle(ideaService.getDefaultInternalProductLineTitle()) == null) {
            productLineRepository.save(new ProductLine(ideaService.getDefaultInternalProductLineTitle()));
        }
        Arrays.stream(productLines).forEach(productLine -> {
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
}
