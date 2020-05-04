package de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions.ProductLineNotFoundException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.ProductLine;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.ideas.ProductLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductLineService {

    @Autowired
    private ProductLineRepository productLineRepository;


    public ProductLine findById(Long id) {
        return productLineRepository.findById(id).orElseThrow(ProductLineNotFoundException::new);
    }

    public ProductLine save(ProductLine productLine) {
        return productLineRepository.save(productLine);
    }
}
