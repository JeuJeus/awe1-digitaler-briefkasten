package de.fhdw.geiletypengmbh.digitalerbriefkasten.controller;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions.IdeaNotFoundException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.Idea;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.IdeaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomepageController {
    @Value("${spring.application.name}")
    String appName;

    @Autowired
    private IdeaRepository ideaRepository;

    @GetMapping("/home")
    public String homePage(Model model) {
        //TODO to be removed -> set home somewhere
        model.addAttribute("appName", appName);
        return "home";
    }

    @GetMapping("/ideas/{id}")
    public ModelAndView findOne(@PathVariable Long id) {
        Idea idea = ideaRepository.findById(id).orElseThrow(IdeaNotFoundException::new);
        ModelAndView mav = new ModelAndView("idea");
        mav.addObject("idea", idea);
        return mav;
    }
}
