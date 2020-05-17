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

    @PostMapping("/createInternal")
    public String createInternalIdea(@ModelAttribute InternalIdea idea) {
        ideaService.createByForm(idea);
        return "redirect:/ideas";
    }

    @PostMapping("/createProduct")
    public String createProductIdea(@ModelAttribute ProductIdea idea) {
        ideaService.createByForm(idea);
        return "redirect:/ideas";
    }

    @PreAuthorize("hasRole('ROLE_SPECIALIST')")
    @PostMapping("/decideIdea/{id}")
    public String decideIdea(@ModelAttribute Idea emptyIdeaWithDecision, @PathVariable Long id) {
        ideaService.saveDecision(id, emptyIdeaWithDecision);
        return "redirect:/specialist";
    }

    @PostMapping("/submitIdea")
    public String submitIdea(@ModelAttribute Idea ideaId) throws InternalProductLineNotExistingException {
        ideaService.submitIdea(ideaId.getId());
        return "redirect:/ideas";
    }

    @PostMapping("/updateIdea/internal/{id}")
    public String updateInternalIdeaByForm(@ModelAttribute InternalIdea idea, @PathVariable Long id) {
        ideaService.updateIdea(idea, id);
        return "redirect:/ideas";
    }

    @PostMapping("/updateIdea/product/{id}")
    public String updateProductIdeaByForm(@ModelAttribute ProductIdea idea, @PathVariable Long id) {
        ideaService.updateIdea(idea, id);
        return "redirect:/ideas";
    }

    @PostMapping("/deleteIdea/{id}")
    public void deleteByUI(@PathVariable Long id, HttpServletRequest request) {
        ideaService.delete(id, request);
    }
}


