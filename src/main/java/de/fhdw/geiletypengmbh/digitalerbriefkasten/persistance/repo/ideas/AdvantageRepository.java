package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.ideas;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.Advantage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdvantageRepository extends JpaRepository<Advantage, Long> {
    List<Advantage> findByDescription(String description);
}
