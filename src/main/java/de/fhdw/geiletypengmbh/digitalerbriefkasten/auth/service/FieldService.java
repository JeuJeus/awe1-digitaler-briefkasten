package de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions.FieldNotFoundException;
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

    public Field findById(Long id) {
        return fieldRepository.findById(id).orElseThrow(FieldNotFoundException::new);
    }

    public Field findByTitle(String title) {
        return fieldRepository.findByTitle(title);
    }

    public List<Field> findAll() {
        return fieldRepository.findAll();
    }

    public Field save(Field field) {
        return fieldRepository.save(field);
    }
}
