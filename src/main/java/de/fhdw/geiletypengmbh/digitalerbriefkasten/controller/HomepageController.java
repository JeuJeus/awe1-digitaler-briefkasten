package de.fhdw.geiletypengmbh.digitalerbriefkasten.controller;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service.IdeaService;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.User;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.*;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.DistributionChannelRepository;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.FieldRepository;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.TargetGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

//import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@Controller
public class HomepageController {

    @Autowired
    private IdeaService ideaService;

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private DistributionChannelRepository distributionChannelRepository;

    @Autowired
    private TargetGroupRepository targetGroupRepository;


    @GetMapping("/ideas/{id}")
    public ModelAndView showOne(@PathVariable Long id) {
        Idea idea = ideaService.findById(id);

        ModelAndView mav = new ModelAndView("idea");
        mav.addObject("idea", idea);
        return mav;
    }

    @GetMapping("/ideas")
    public ModelAndView showAllForLoggedInUser() {

        List<Idea> submittedIdeas = ideaService.getSubmittedIdeas();
        List<Idea> productIdeas = ideaService.filterProductIdeas(submittedIdeas);
        List<Idea> internalIdeas = ideaService.filterInternalIdeas(submittedIdeas);
        List<Idea> notSubmittedIdeas = ideaService.GetOwnNotSubmittedIdeas();

        ModelAndView mav = new ModelAndView("ideas");
        mav.addObject("productIdeas", productIdeas);
        mav.addObject("internalIdeas", internalIdeas);
        mav.addObject("notSubmittedIdeas", notSubmittedIdeas);

        return mav;
    }

    @GetMapping("/createIdea/internal")
    public ModelAndView createInternalIdea() {
        List<Field> fields = fieldRepository.findAll();

        ModelAndView mav = new ModelAndView("createIdea/internal");
        mav.addObject("fields", fields);
        mav.addObject("createIdea", new InternalIdea());
        mav.addObject("advantage", new Advantage());

        return mav;
    }

    @GetMapping("/createIdea/product")
    public ModelAndView createProductIdea() {
        //TODO ADD ERROR HANDLING -> DUPLICATE IDEA TITLE SHOULD BE SPECIFIC ERROR
        List<DistributionChannel> distributionChannels = distributionChannelRepository.findAll();
        List<TargetGroup> targetGroups = targetGroupRepository.findAll();

        ModelAndView mav = new ModelAndView("createIdea/product");
        mav.addObject("distributionChannels", distributionChannels);
        mav.addObject("targetGroups", targetGroups);
        mav.addObject("createIdea", new ProductIdea());

        return mav;
    }
}
