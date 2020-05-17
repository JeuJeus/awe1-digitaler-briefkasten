package de.fhdw.geiletypengmbh.digitalerbriefkasten.controller;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.InternalProductLineNotExistingException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.Idea;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.InternalIdea;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.ProductIdea;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.service.ideas.IdeaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IdeaController {

    @Autowired
    private IdeaService ideaService;

    @PreAuthorize("hasRole('ROLE_SPECIALIST')")
    @PostMapping("/decideIdea/{id}")
    public Idea decideIdea(@ModelAttribute Idea emptyIdeaWithDecision, @PathVariable Long id) {
        return ideaService.saveDecision(id, emptyIdeaWithDecision);
    }

    @PostMapping("/submitIdea")
    public String submitIdea(@ModelAttribute Idea ideaId) throws InternalProductLineNotExistingException {
        ideaService.submitIdea(ideaId.getId());
        return "redirect:/ideas/";
    }

    @PostMapping("/updateIdea/internal/{id}")
    public Idea updateInternalIdeaByForm(@ModelAttribute InternalIdea idea, @PathVariable Long id) {
        return ideaService.updateIdea(idea, id);
    }

    @PostMapping("/updateIdea/product/{id}")
    public Idea updateProductIdeaByForm(@ModelAttribute ProductIdea idea, @PathVariable Long id) {
        return ideaService.updateIdea(idea, id);
    }

    @PostMapping("/deleteIdea/{id}")
    public void deleteByUI(@PathVariable Long id, HttpServletRequest request) {
        ideaService.delete(id, request);
    }
}


