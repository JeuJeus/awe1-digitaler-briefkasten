package de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions.IdeaIdMismatchException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions.IdeaMalformedException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions.IdeaNotFoundException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions.NotAuthorizedException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.Idea;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.Status;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.User;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.Idea;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.InternalIdea;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.ProductIdea;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.Status;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.User;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.IdeaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class IdeaService {

    @Autowired
    private IdeaRepository<InternalIdea> internalIdeaIdeaRepository;

    @Autowired
    private IdeaRepository<ProductIdea> productIdeaRepository;

    @Autowired
    private UserService userService;

    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        return userService.findByUsername(username);
    }

    public List<Idea> findAll() {
        List<Idea> allInternalIdeas = internalIdeaIdeaRepository.findAll();
        List<Idea> allProductIdeas = productIdeaRepository.findAll();
        List<Idea> allIdeas = new ArrayList<>();
        allIdeas.addAll(allInternalIdeas);
        allIdeas.addAll(allProductIdeas);
        return allIdeas;
    }

    public List<Idea> findByTitle(String ideaTitle) {
        List<Idea> internalIdeas = internalIdeaIdeaRepository.findByTitle(ideaTitle);
        List<Idea> productIdeas = productIdeaRepository.findByTitle(ideaTitle);
        List<Idea> ideas = new ArrayList<>();
        ideas.addAll(internalIdeas);
        ideas.addAll(productIdeas);
        return ideas;
    }

    public Idea findById(Long id) {
        Optional<Idea> idea = internalIdeaIdeaRepository.findById(id);
        if (idea.isEmpty()) {
            idea = productIdeaRepository.findById(id);
            if (idea.isEmpty()) {
                throw new IdeaNotFoundException();
            }
        }
        return idea.orElseThrow(IdeaNotFoundException::new);
    }

    public Idea save(Idea idea) {
        //Only logged in Users are able to create Ideas
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            try {
                if (idea instanceof InternalIdea) return internalIdeaIdeaRepository.saveAndFlush(idea);
                else if (idea instanceof ProductIdea) {
                    return productIdeaRepository.saveAndFlush(idea);
                }
            } catch (Exception e) {
                throw new IdeaMalformedException(e);
            }
        } else {
            throw new NotAuthorizedException();
        }
        return null; //TODO null okay hier?
    }

    public void delete(Long id, HttpServletRequest request) {
        Optional<Idea> idea = internalIdeaIdeaRepository.findById(id);
        Idea toDelete;
        if (idea.isEmpty()) {
            idea = productIdeaRepository.findById(id);
        }
        toDelete = idea.orElseThrow(IdeaNotFoundException::new);
        User currentUser = getCurrentUser();
        boolean userRightful = toDelete.getCreator().equals(currentUser)
                && toDelete.getStatus().equals(Status.NOT_SUBMITTED);

        /* There are 2 legitimate states to delete a Idea
            -> either as an ADMIN "with Godpower"
            -> or as THE CREATOR and if the idea was NOT_SUBMITTED (= still in draftmode)
        */
        if (request.isUserInRole("ADMIN") || userRightful) {
            if (toDelete instanceof InternalIdea) internalIdeaIdeaRepository.delete(toDelete);
            else if (toDelete instanceof ProductIdea) productIdeaRepository.delete(toDelete);
        }
        else {
            throw new NotAuthorizedException();
        }
    }

    public Idea updateIdea(Idea idea, Long id) {
        if (idea.getId() != id) {
            throw new IdeaIdMismatchException();
        }
        Idea checkIdea = this.findById(id);
        if (idea instanceof InternalIdea) return internalIdeaIdeaRepository.saveAndFlush(checkIdea);
        else if (idea instanceof ProductIdea) return productIdeaRepository.saveAndFlush(idea);
        return null; // TODO null okay hier?
    }

    public Idea createByForm(Idea idea) {
        idea.setCreator(getCurrentUser());
        return save(idea);
    }

    public List<Idea> getSubmittedIdeas() {
        List<Idea> allIdeas = findAll();
        Predicate<Idea> ideaIsNotSubmitted = idea -> idea.getStatus().equals(Status.NOT_SUBMITTED);

        return allIdeas.stream().
                filter(Predicate.not(ideaIsNotSubmitted))
                .collect(Collectors.toList());
    }

    public List<Idea> filterProductIdeas(List<Idea> ideas) {
        List<Idea> productIdeas = new ArrayList<Idea>();
        Predicate<Idea> ideaIsProductIdea = idea -> idea instanceof ProductIdea;
        productIdeas = ideas.stream().
                filter(ideaIsProductIdea)
                .collect(Collectors.toList());
        return productIdeas;
    }

    public List<Idea> filterInternalIdeas(List<Idea> ideas) {
        List<Idea> internalIdeas = new ArrayList<Idea>();
        Predicate<Idea> ideaIsInternalIdea = idea -> idea instanceof InternalIdea;
        internalIdeas = ideas.stream().
                filter(ideaIsInternalIdea)
                .collect(Collectors.toList());
        return internalIdeas;
    }

    public List<Idea> GetOwnNotSubmittedIdeas() {
        List<Idea> allIdeas = findAll();

        Predicate<Idea> ideaBelongsToCurUser = idea -> idea.getCreator().getId() == getCurrentUser().getId();
        Predicate<Idea> ideaIsNotSubmitted = idea -> idea.getStatus().equals(Status.NOT_SUBMITTED);

        return allIdeas.stream().
                filter(ideaBelongsToCurUser.and(ideaIsNotSubmitted))
                .collect(Collectors.toList());
    }
}
