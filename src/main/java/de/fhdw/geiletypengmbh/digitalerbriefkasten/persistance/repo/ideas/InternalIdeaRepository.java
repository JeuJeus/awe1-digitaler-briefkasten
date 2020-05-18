//Autor: JF
package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.ideas;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.InternalIdea;

import java.util.List;

public interface InternalIdeaRepository extends IdeaRepository<InternalIdea> {
    List<InternalIdea> findByTitle(String title);
}
