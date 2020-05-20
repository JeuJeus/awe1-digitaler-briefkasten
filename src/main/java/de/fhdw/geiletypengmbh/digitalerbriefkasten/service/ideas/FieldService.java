//Autor: PR
package de.fhdw.geiletypengmbh.digitalerbriefkasten.service.ideas;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.AlreadyExistsException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.FieldNotFoundException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.Field;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.ideas.FieldRepository;
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
        if (findByTitle(field.getTitle()) == null) {
            return fieldRepository.save(field);
        } else {
            throw new AlreadyExistsException("Handlungsfeld existiert bereits.");
        }
    }
}
