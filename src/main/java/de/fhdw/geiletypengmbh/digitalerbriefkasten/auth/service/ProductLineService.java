package de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.ProductLine;

import java.util.List;

public interface ProductLineService {
    ProductLine findByTitle(String title);
}
