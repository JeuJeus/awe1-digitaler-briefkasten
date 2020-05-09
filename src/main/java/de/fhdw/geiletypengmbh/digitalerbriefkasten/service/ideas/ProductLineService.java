package de.fhdw.geiletypengmbh.digitalerbriefkasten.service.ideas;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.ProductLineNotFoundException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.ProductLine;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.ideas.ProductLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<ProductLine> findByTitle(String title) {
        return productLineRepository.findByTitle(title);
    }

}
