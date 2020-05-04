package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.ideas;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.ProductIdea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductIdeaRepository extends IdeaRepository<ProductIdea> {
    List<ProductIdea> findByTitle(String title);
}
