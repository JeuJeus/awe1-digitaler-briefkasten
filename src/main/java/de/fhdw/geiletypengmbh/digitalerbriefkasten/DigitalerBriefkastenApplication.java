package de.fhdw.geiletypengmbh.digitalerbriefkasten;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo")
@EntityScan("de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model")
@SpringBootApplication
public class DigitalerBriefkastenApplication {

    public static void main(String[] args) {
        //TODO refactor whole project before "Abgabe" -> in Intellij rightclick project Root
        SpringApplication.run(DigitalerBriefkastenApplication.class, args);
    }

}
