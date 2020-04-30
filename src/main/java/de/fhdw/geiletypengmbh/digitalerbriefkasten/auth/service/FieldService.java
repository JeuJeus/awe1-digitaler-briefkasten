package de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.Field;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.Field;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.FieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FieldService {

    @Autowired
    private FieldRepository fieldRepository;

    Field findByTtitle(String title) {
        return fieldRepository.findByTitle(title);
    }

    public List<Field> findAll() {
        return fieldRepository.findAll();
    }

    public Field save(Field field) {
        return fieldRepository.save(field);
    }
}
