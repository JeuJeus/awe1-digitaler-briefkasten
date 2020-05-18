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

    //Autor: JB
    @PostMapping("/createInternal")
    public String createInternalIdea(@ModelAttribute InternalIdea idea) {
        ideaService.createByForm(idea);
        return "redirect:/ideas";
    }

    //Autor: JB
    @PostMapping("/createProduct")
    public String createProductIdea(@ModelAttribute ProductIdea idea) {
        ideaService.createByForm(idea);
        return "redirect:/ideas";
    }

    //Autor: JF
    @PreAuthorize("hasRole('ROLE_SPECIALIST')")
    @PostMapping("/decideIdea/{id}")
    public String decideIdea(@ModelAttribute Idea emptyIdeaWithDecision, @PathVariable Long id) {
        ideaService.saveDecision(id, emptyIdeaWithDecision);
        return "redirect:/specialist";
    }

    //Autor: JB
    @PostMapping("/submitIdea/{id}")
    public String submitIdea(@PathVariable Long id) throws InternalProductLineNotExistingException {
        ideaService.submitIdea(id);
        return "redirect:/ideas";
    }

    //Autor: PR
    @PostMapping("/updateIdea/internal/{id}")
    public String updateInternalIdeaByForm(@ModelAttribute InternalIdea idea, @PathVariable Long id) {
        ideaService.updateIdea(idea, id);
        return "redirect:/ideas";
    }

    //Autor: PR
    @PostMapping("/updateIdea/product/{id}")
    public String updateProductIdeaByForm(@ModelAttribute ProductIdea idea, @PathVariable Long id) {
        ideaService.updateIdea(idea, id);
        return "redirect:/ideas";
    }

    //Autor: PR
    @PostMapping("/deleteIdea/{id}")
    public void deleteByUI(@PathVariable Long id, HttpServletRequest request) {
        ideaService.delete(id, request);
    }
}


