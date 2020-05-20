package de.fhdw.geiletypengmbh.digitalerbriefkasten.service.ideas;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.AlreadyExistsException;
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

    @Autowired
    private IdeaService ideaService;

    //Autor: PR
    public ProductLine findById(Long id) {
        return productLineRepository.findById(id).orElseThrow(ProductLineNotFoundException::new);
    }
    //Autor: JB
    public ProductLine findByTitle(String title) {
        return productLineRepository.findByTitle(title);
    }

    //Autor: JB
    public ProductLine save(ProductLine productLine) {
        if (findByTitle(productLine.getTitle()) == null) {
            return productLineRepository.save(productLine);
        } else {
            throw new AlreadyExistsException("Produktsparte existiert bereits.");
        }
    }

    //Autor: PR
    public List<ProductLine> findAll() {
        return productLineRepository.findAll();
    }

    //Autor: PR
    public List<ProductLine> findAllByTitleNot(String title) {
        return productLineRepository.findAllByTitleNot(title);
    }

    //Autor: PR
    public List<ProductLine> findAllExceptInternal() {
        return productLineRepository.findAllByTitleNot(ideaService.getDefaultInternalProductLineTitle());
    }

}
