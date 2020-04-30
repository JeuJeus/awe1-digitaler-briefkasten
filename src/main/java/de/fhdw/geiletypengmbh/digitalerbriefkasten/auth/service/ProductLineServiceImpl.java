package de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.User;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.ProductLine;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.ProductLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class ProductLineServiceImpl implements ProductLineService {

    @Autowired
    private ProductLineRepository productLineRepository;

    @Override
    public void save(ProductLine productLine) {
        productLineRepository.save(productLine);
    }

}
