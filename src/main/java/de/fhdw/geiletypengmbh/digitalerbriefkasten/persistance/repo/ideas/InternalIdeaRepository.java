package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.ideas;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.InternalIdea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InternalIdeaRepository extends IdeaRepository<InternalIdea> {
    List<InternalIdea> findByTitle(String title);
}
