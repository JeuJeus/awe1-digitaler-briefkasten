package de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.ProductLine;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.ProductLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public class ProductLineService {

    @Autowired
    private ProductLineRepository productLineRepository;

    void save(ProductLine productLine) {
        productLineRepository.save(productLine);
    }
}
