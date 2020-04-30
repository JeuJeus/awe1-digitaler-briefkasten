package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.User;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.Advantage;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.Field;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdvantageRepository extends JpaRepository<Advantage, Long> {
    Field findByDescription(String description);

    List<Advantage> findAll();
}
