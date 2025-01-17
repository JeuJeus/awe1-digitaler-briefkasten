package de.fhdw.geiletypengmbh.digitalerbriefkasten.controller;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.NotAuthorizedException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ContactMessage;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.*;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.service.ContactMessageService;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.service.account.UserServiceImpl;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.service.ideas.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomepageController {

    @Autowired
    private IdeaService ideaService;

    @Autowired
    private FieldService fieldService;

    @Autowired
    private DistributionChannelService distributionChannelService;

    @Autowired
    private TargetGroupService targetGroupService;

    @Autowired
    private ProductLineService productLineService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ContactMessageService contactMessageService;

    //Autor: PR
    @GetMapping("/ideas/{id}")
    public ModelAndView showOne(@PathVariable Long id) {
        Idea idea = ideaService.findById(id);
        String view = "ideas/idea";
        ModelAndView mav = new ModelAndView(view);
        mav.addObject("idea", idea);
        return mav;
    }

    //Autor: JF
    @PreAuthorize("hasRole('ROLE_SPECIALIST')")
    @GetMapping("/decideIdea/{id}")
    public ModelAndView getOneToDecide(@PathVariable Long id) {
        Idea idea = ideaService.findById(id);
        StatusDecision statusDecision = new StatusDecision();

        String view = "ideas/decideIdea";
        ModelAndView mav = new ModelAndView(view);
        mav.addObject("idea", idea);
        mav.addObject("statuses", ideaService.getViableStatusesForDecision(idea.getStatus()));
        mav.addObject("statusDecision", statusDecision);
        return mav;
    }

    //Autor: PR
    @GetMapping("/ideas")
    public ModelAndView showAllForLoggedInUser(Principal user) {
        List<Idea> submittedIdeas = ideaService.getSubmittedIdeas();
        List<Idea> productIdeas = ideaService.filterProductIdeas(submittedIdeas);
        List<Idea> internalIdeas = ideaService.filterInternalIdeas(submittedIdeas);

        ModelAndView mav = new ModelAndView("ideas/ideas");
        mav.addObject("productIdeas", productIdeas);
        mav.addObject("internalIdeas", internalIdeas);

        if (user != null) {
            List<Idea> notSubmittedIdeas = ideaService.getOwnNotSubmittedIdeas();
            mav.addObject("notSubmittedIdeas", notSubmittedIdeas);
        }

        return mav;
    }

    //Autor: JB
    @GetMapping("/createIdea/internal")
    public ModelAndView createInternalIdea() {
        List<Field> fields = fieldService.findAll();

        ModelAndView mav = new ModelAndView("ideas/createInternal");
        mav.addObject("fields", fields);
        mav.addObject("createIdea", new InternalIdea());
        mav.addObject("advantage", new Advantage());

        return mav;
    }

    //Autor: JB
    @GetMapping("/createIdea/product")
    public ModelAndView createProductIdea() {
        List<DistributionChannel> distributionChannels = distributionChannelService.findAll();
        List<TargetGroup> targetGroups = targetGroupService.findAll();

        List<ProductLine> productLines = productLineService.findAll();

        ModelAndView mav = new ModelAndView("ideas/createProduct");
        mav.addObject("distributionChannels", distributionChannels);
        mav.addObject("targetGroups", targetGroups);
        mav.addObject("productLines", productLines);
        mav.addObject("createIdea", new ProductIdea());
        mav.addObject("advantage", new Advantage());
        mav.addObject("internalProductLine", ideaService.getDefaultInternalProductLineTitle());

        return mav;
    }

    //Autor: PR
    @GetMapping("/ideas/edit/{id}")
    public ModelAndView editOne(@PathVariable Long id) {
        Idea idea = ideaService.findById(id);

        if (!ideaService.getIfIdeaCanBeEdited(idea)) {
            throw new NotAuthorizedException();
        }
        ModelAndView mav = new ModelAndView();
        String view;
        ArrayList<ProductLine> productLines;

        if (idea instanceof InternalIdea) {
            view = "ideas/editInternalIdea";
            mav.addObject("fields", fieldService.findAll());
            productLines = (ArrayList<ProductLine>) productLineService.findAll();
        } else {
            view = "ideas/editProductIdea";
            mav.addObject("targetGroups", targetGroupService.findAll());
            mav.addObject("distributionChannels", distributionChannelService.findAll());
            productLines = (ArrayList<ProductLine>) productLineService.findAllExceptInternal();
        }
        mav.setViewName(view);
        mav.addObject("productLines", productLines);
        mav.addObject("idea", idea);
        return mav;
    }

    @GetMapping("/contact")
    public ModelAndView contactForm() {
        ContactMessage contactMessage = new ContactMessage();
        ModelAndView mav = new ModelAndView("contactForm");
        mav.addObject("contactMessage", contactMessage);
        return mav;
    }

    @GetMapping("/contact/{id}")
    public ModelAndView contactMessage(@PathVariable Long id) {
        ContactMessage contactMessage = contactMessageService.findById(id);
        ModelAndView mav = new ModelAndView("contactMessage");
        mav.addObject("contactMessage", contactMessage);
        return mav;
    }
}
