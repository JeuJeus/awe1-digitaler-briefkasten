package de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions.*;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.User;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.Idea;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.InternalIdea;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.ProductIdea;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.Status;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.IdeaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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

    private User getCurrentUser() throws UserNotFoundException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        return userService.findByUsername(username);
    }

    public List<Idea> findAll() {
        List<Idea> allIdeas = new ArrayList<>();
        allIdeas.addAll(internalIdeaIdeaRepository.findAll());
        allIdeas.addAll(productIdeaRepository.findAll());
        return allIdeas;
    }

    public List<Idea> findByTitle(String ideaTitle) {
        List<Idea> ideas = new ArrayList<>();
        ideas.addAll(internalIdeaIdeaRepository.findByTitle(ideaTitle));
        ideas.addAll(productIdeaRepository.findByTitle(ideaTitle));
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
        try {
            getCurrentUser();
            if (idea instanceof InternalIdea) return internalIdeaIdeaRepository.saveAndFlush(idea);
                //then -> idea instanceof ProductIdea
            else return productIdeaRepository.saveAndFlush(idea);
        } catch (UserNotFoundException e) {
            throw new NotAuthorizedException();
        } catch (Exception e) {
            throw new IdeaMalformedException(e);
        }
    }


    public void delete(Long id, HttpServletRequest request) {
        Idea toDelete = this.findById(id);


        /* There are 2 legitimate states to delete a Idea
            -> either as an ADMIN "with Godpower"
            -> OR as THE CREATOR AND if the idea was NOT_SUBMITTED (= still in draftmode)
        */
        try {
            User currentUser = getCurrentUser();
            if (request.isUserInRole("ADMIN") ||
                    (toDelete.getCreator().equals(currentUser)
                            && toDelete.getStatus().equals(Status.NOT_SUBMITTED))) {
                if (toDelete instanceof InternalIdea) internalIdeaIdeaRepository.delete(toDelete);
                else if (toDelete instanceof ProductIdea) productIdeaRepository.delete(toDelete);
            } else {
                throw new NotAuthorizedException();
            }
        } catch (UserNotFoundException e) {
            throw new NotAuthorizedException();
        }

    }

    public Idea updateIdea(Idea idea, Long id) {
        if (idea.getId() != id) {
            throw new IdeaIdMismatchException();
        }
        //TODO CHECK IF IDEA HAS CHANGE IN MEANTIME
        Idea checkExistantIdea = this.findById(id);
        if (idea instanceof InternalIdea) return internalIdeaIdeaRepository.saveAndFlush(idea);
            //then -> idea instanceof ProductIdea
        else return productIdeaRepository.saveAndFlush(idea);
    }

    public Idea createByForm(Idea idea) {
        try {
            idea.setCreator(getCurrentUser());
        } catch (UserNotFoundException e) {
            throw new NotAuthorizedException();
        }
        try {
            if (idea instanceof InternalIdea) return internalIdeaIdeaRepository.saveAndFlush(idea);
                //then -> idea instanceof ProductIdea
            else return productIdeaRepository.saveAndFlush(idea);
        } catch (Exception e) {
            throw new IdeaMalformedException(e);
        }
    }

    public List<Idea> getSubmittedIdeas() {
        List<Idea> allIdeas = findAll();

        Predicate<Idea> ideaIsNotSubmitted = idea -> idea.getStatus().equals(Status.NOT_SUBMITTED);

        return allIdeas.stream().
                filter(Predicate.not(ideaIsNotSubmitted))
                .collect(Collectors.toList());
    }

    public List<Idea> filterProductIdeas(List<Idea> ideas) {
        Predicate<Idea> ideaIsProductIdea = idea -> idea instanceof ProductIdea;

        return ideas.stream().
                filter(ideaIsProductIdea)
                .collect(Collectors.toList());
    }

    public List<Idea> filterInternalIdeas(List<Idea> ideas) {
        Predicate<Idea> ideaIsInternalIdea = idea -> idea instanceof InternalIdea;

        return ideas.stream().
                filter(ideaIsInternalIdea)
                .collect(Collectors.toList());
    }

    public List<Idea> GetOwnNotSubmittedIdeas() {
        List<Idea> allIdeas = findAll();

        Predicate<Idea> ideaBelongsToCurUser = idea -> {
            try {
                return idea.getCreator().getId() == getCurrentUser().getId();
            } catch (UserNotFoundException e) {
                throw new NotAuthorizedException();
            }
        };
        Predicate<Idea> ideaIsNotSubmitted = idea -> idea.getStatus().equals(Status.NOT_SUBMITTED);

        return allIdeas.stream().
                filter(ideaBelongsToCurUser.and(ideaIsNotSubmitted))
                .collect(Collectors.toList());
    }
}
