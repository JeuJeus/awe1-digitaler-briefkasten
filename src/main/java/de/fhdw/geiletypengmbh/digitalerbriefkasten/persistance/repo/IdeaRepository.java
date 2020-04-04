package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.Idea;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IdeaRepository extends CrudRepository<Idea, Long> {
    List<Idea> findByTitle(String title);
}
