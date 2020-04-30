package de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions.*;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.User;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.*;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.AdvantageRepository;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.IdeaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
        return advantageRepository.findAll();
    }

    public Advantage save(Advantage advantage) {
        return advantageRepository.save(advantage);
    }
}