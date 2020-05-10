package de.fhdw.geiletypengmbh.digitalerbriefkasten.controller;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.NotAuthorizedException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.UserNotFoundException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.User;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.*;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.ideas.DistributionChannelRepository;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.ideas.FieldRepository;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.ideas.ProductLineRepository;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.ideas.TargetGroupRepository;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.service.account.UserServiceImpl;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.service.ideas.IdeaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
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
    private Object Status;


    @GetMapping("/ideas/{id}")
    public ModelAndView showOne(@PathVariable Long id) {
        Idea idea = ideaService.findById(id);
        String view = idea instanceof InternalIdea ? "ideas/internalIdea" : "ideas/ProductIdea";
        ModelAndView mav = new ModelAndView(view);
        mav.addObject("idea", idea);
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_SPECIALIST')")
    @GetMapping("/decideIdea/{id}")
    public ModelAndView getOneToDecide(@PathVariable Long id) throws UserNotFoundException {
        Idea idea = ideaService.findById(id);
        if (!idea.getSpecialist().getUsername()
                .equals(userService.getCurrentUser().getUsername())) throw new NotAuthorizedException();

        String view = "ideas/decideIdea";
        ModelAndView mav = new ModelAndView(view);
        mav.addObject("ideaToDecide", idea);
        return mav;
    }

    @GetMapping("/ideas")
    public ModelAndView showAllForLoggedInUser(Principal user) {
        List<Idea> submittedIdeas = ideaService.getSubmittedIdeas();
        List<Idea> productIdeas = ideaService.filterProductIdeas(submittedIdeas);
        List<Idea> internalIdeas = ideaService.filterInternalIdeas(submittedIdeas);

        ModelAndView mav = new ModelAndView("ideas/ideas");
        mav.addObject("productIdeas", productIdeas);
        mav.addObject("internalIdeas", internalIdeas);

        if(user != null){
            List<Idea> notSubmittedIdeas = ideaService.GetOwnNotSubmittedIdeas();
            mav.addObject("notSubmittedIdeas", notSubmittedIdeas);
        }

        return mav;
    }

    @GetMapping("/createIdea/internal")
    public ModelAndView createInternalIdea() {
        List<Field> fields = fieldRepository.findAll();
        List<ProductLine> productLines = productLineRepository.findAll();

        ModelAndView mav = new ModelAndView("ideas/createInternal");
        mav.addObject("fields", fields);
        mav.addObject("productLines", productLines);
        mav.addObject("createIdea", new InternalIdea());
        mav.addObject("advantage", new Advantage());

        return mav;
    }

    @GetMapping("/createIdea/product")
    public ModelAndView createProductIdea() throws UserNotFoundException {
        List<DistributionChannel> distributionChannels = distributionChannelRepository.findAll();
        List<TargetGroup> targetGroups = targetGroupRepository.findAll();

        List<ProductLine> productLines = productLineRepository.findAll();

        ModelAndView mav = new ModelAndView("ideas/createProduct");
        mav.addObject("distributionChannels", distributionChannels);
        mav.addObject("targetGroups", targetGroups);
        mav.addObject("productLines", productLines);
        mav.addObject("createIdea", new ProductIdea());
        mav.addObject("advantage", new Advantage());

        return mav;
    }

    @GetMapping("/ideas/edit/{id}")
    public ModelAndView editOne(@PathVariable Long id) throws UserNotFoundException {
        Idea idea = ideaService.findById(id);
        User currentUser = userService.getCurrentUser();
        if (currentUser.getId() != idea.getCreator().getId()) throw new NotAuthorizedException();
        String view = idea instanceof InternalIdea ? "ideas/editInternalIdea" : "ideas/editProductIdea";
        ModelAndView mav = new ModelAndView(view);
        mav.addObject("idea", idea);
        return mav;
    }
}
