package de.fhdw.geiletypengmbh.digitalerbriefkasten.controller;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions.UserNotFoundException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.Specialist;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.*;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.ideas.DistributionChannelRepository;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.ideas.FieldRepository;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.ideas.ProductLineRepository;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.ideas.TargetGroupRepository;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.service.account.UserServiceImpl;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.service.ideas.IdeaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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

    @Autowired
    private ProductLineRepository productLineRepository;

    @Autowired
    private UserServiceImpl userService;


    @GetMapping("/ideas/{id}")
    public ModelAndView showOne(@PathVariable Long id) throws UserNotFoundException {
        Idea idea = ideaService.findById(id);
        String view = idea instanceof InternalIdea ? "ideas/internalIdea" : "ideas/ProductIdea";
        ModelAndView mav = new ModelAndView(view);
        mav.addObject("idea", idea);
        mav.addObject("specialist", userService.getCurrentUser() instanceof Specialist);
        return mav;
    }

    @GetMapping("/ideas")
    public ModelAndView showAllForLoggedInUser() throws UserNotFoundException {

        List<Idea> submittedIdeas = ideaService.getSubmittedIdeas();
        List<Idea> productIdeas = ideaService.filterProductIdeas(submittedIdeas);
        List<Idea> internalIdeas = ideaService.filterInternalIdeas(submittedIdeas);
        List<Idea> notSubmittedIdeas = ideaService.GetOwnNotSubmittedIdeas();

        ModelAndView mav = new ModelAndView("ideas/ideas");
        mav.addObject("productIdeas", productIdeas);
        mav.addObject("internalIdeas", internalIdeas);
        mav.addObject("notSubmittedIdeas", notSubmittedIdeas);
        mav.addObject("specialist", userService.getCurrentUser() instanceof Specialist);

        return mav;
    }

    @GetMapping("/createIdea/internal")
    public ModelAndView createInternalIdea() throws UserNotFoundException {
        List<Field> fields = fieldRepository.findAll();
        List<ProductLine> productLines = productLineRepository.findAll();

        ModelAndView mav = new ModelAndView("ideas/createInternal");
        mav.addObject("fields", fields);
        mav.addObject("productLines", productLines);
        mav.addObject("createIdea", new InternalIdea());
        mav.addObject("advantage", new Advantage());
        mav.addObject("specialist", userService.getCurrentUser() instanceof Specialist);

        return mav;
    }

    @GetMapping("/createIdea/product")
    public ModelAndView createProductIdea() throws UserNotFoundException {
        //TODO ADD ERROR HANDLING -> DUPLICATE IDEA TITLE SHOULD BE SPECIFIC ERROR
        List<DistributionChannel> distributionChannels = distributionChannelRepository.findAll();
        List<TargetGroup> targetGroups = targetGroupRepository.findAll();

        List<ProductLine> productLines = productLineRepository.findAll();

        ModelAndView mav = new ModelAndView("ideas/createProduct");
        mav.addObject("distributionChannels", distributionChannels);
        mav.addObject("targetGroups", targetGroups);
        mav.addObject("productLines", productLines);
        mav.addObject("createIdea", new ProductIdea());
        mav.addObject("advantage", new Advantage());
        mav.addObject("specialist", userService.getCurrentUser() instanceof Specialist);

        return mav;
    }
}
