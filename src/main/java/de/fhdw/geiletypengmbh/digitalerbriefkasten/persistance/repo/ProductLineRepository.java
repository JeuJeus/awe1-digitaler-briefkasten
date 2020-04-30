package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.User;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.ProductLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductLineRepository extends JpaRepository<ProductLine, Long> {
    List<ProductLine> findAll();

    List<ProductLine> findByTitle(String title);
}