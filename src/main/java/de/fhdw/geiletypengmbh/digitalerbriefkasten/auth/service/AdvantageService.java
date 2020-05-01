package de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions.AdvantageNotFoundException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.Advantage;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.AdvantageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdvantageService {

    @Autowired
    private AdvantageRepository advantageRepository;

    public Advantage findById(Long id) {
        return advantageRepository.findById(id).orElseThrow(AdvantageNotFoundException::new);
    }

    public List<Advantage> findByDescription(String description) {
        return advantageRepository.findByDescription(description);
    }

    public List<Advantage> findAll() {
        //TODO CHECK WHY WE WOULD NEED THIS -> ALL ADVANTAGES BUT WITHOUT CONTEXT OF IDEA xd
        return advantageRepository.findAll();
    }

    public Advantage save(Advantage advantage) {
        return advantageRepository.save(advantage);
    }
}
