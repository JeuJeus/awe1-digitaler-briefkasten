package de.fhdw.geiletypengmbh.digitalerbriefkasten.controller;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service.IdeaService;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.Idea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class HomepageController {
    @Value("${spring.application.name}")
    String appName;

    @Autowired
    private IdeaService ideaService;

    @GetMapping("/home")
    public String homePage(Model model) {
        //TODO to be removed -> set home somewhere
        model.addAttribute("appName", appName);
        return "home";
    }

    @GetMapping("/ideas/{id}")
    public ModelAndView showOne(@PathVariable Long id) {
        Idea idea = ideaService.findById(id);

        ModelAndView mav = new ModelAndView("idea");
        mav.addObject("idea", idea);
        return mav;
    }

    @GetMapping("/ideas")
    public ModelAndView showAllForLoggedInUser() {

        List<Idea> ideas = ideaService.findAll();
        List<Idea> notSubmittedIdeas = ideaService.GetOwnNotSubmittedIdeas();

        ModelAndView mav = new ModelAndView("ideas");
        mav.addObject("ideas", ideas);
        mav.addObject("notSubmittedIdeas", notSubmittedIdeas);

        return mav;
    }

    @GetMapping("/createIdea")
    public String createIdea(Model model) {
        //TODO ADD ERROR HANDLING -> DUPLICATE IDEA TITLE THROWS SQL ERROR
        model.addAttribute("createIdea", new Idea());
        return "createIdea";
    }
}
