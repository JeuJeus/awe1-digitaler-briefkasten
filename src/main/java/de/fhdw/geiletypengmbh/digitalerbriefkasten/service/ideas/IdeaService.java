package de.fhdw.geiletypengmbh.digitalerbriefkasten.service.ideas;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.*;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.Specialist;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.User;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.*;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.ideas.IdeaRepository;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.ideas.InternalIdeaRepository;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.ideas.ProductIdeaRepository;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.service.account.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class IdeaService {

    @Autowired
    private InternalIdeaRepository internalIdeaIdeaRepository;

    @Autowired
    private ProductIdeaRepository productIdeaRepository;

    @Autowired
    private IdeaRepository<Idea> ideaRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ProductLineService productLineService;

    private static final String DEFAULT_INTERNAL_PRODUCTLINE_TITLE = "INTERNAL";

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
        assureIdeaAccessRightsForList(ideas);
        return ideas;
    }


    public Idea findById(Long id) {
        Idea idea = ideaRepository.findById(id).orElseThrow(IdeaNotFoundException::new);
        // Only subtypes of Idea should be found
        if (!Idea.class.isAssignableFrom(idea.getClass())) {
            throw new IdeaNotFoundException();
        }
        assureIdeaAccessRights(idea);
        return idea;
    }

    private void assureIdeaAccessRightsForList(List<Idea> ideas) {
        for (Idea idea : ideas) {
            try {
                assureIdeaAccessRights(idea);
            } catch (NotAuthorizedException e) {
                ideas.remove(idea);
            }
        }
    }

    private void assureIdeaAccessRights(Idea idea) {

        boolean ideaIsSubmitted = !idea.getStatus().equals(Status.NOT_SUBMITTED);
        boolean ideaIsInStorage = idea.getStatus().equals(Status.IDEA_STORAGE);
        boolean ideaIsPublic = (idea.getStatus().equals(Status.PENDING) || idea.getStatus().equals(Status.ACCEPTED) || idea.getStatus().equals(Status.DECLINED));

        User currentUser = getUser();
        //TODO REFACTOR NULL AWAY
        if (currentUser != null) {
            boolean currentUserIsCreator = idea.getCreator().getId() == currentUser.getId();
            boolean currentUserIsAdmin = currentUser.isRole("ADMIN");
            boolean currentUserIsSpecialist = currentUser.isRole("SPECIALIST");
            if (!ideaIsPublic && !currentUserIsAdmin) {
                if (!ideaIsSubmitted && !currentUserIsCreator) throw new NotAuthorizedException();
                if (ideaIsInStorage && !currentUserIsSpecialist) throw new NotAuthorizedException();
            }
        } else {
            if (!ideaIsPublic) throw new NotAuthorizedException();
        }
    }

    private User getUser() {
        User currentUser = null;
        try {
            currentUser = userService.getCurrentUser();
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        return currentUser;
    }

    public Idea save(Idea idea) {
        //Only logged in Users are able to create Ideas
        try {
            userService.getCurrentUser();
            return ideaRepository.saveAndFlush(idea);
        } catch (UserNotFoundException e) {
            throw new NotAuthorizedException();
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateIdeaNameException();
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
            User currentUser = userService.getCurrentUser();
            if (request.isUserInRole("ADMIN") ||
                    (toDelete.getCreator().equals(currentUser)
                            && toDelete.getStatus().equals(Status.NOT_SUBMITTED))) {
                ideaRepository.delete(toDelete);
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
        Idea oldIdea = findById(id);
        oldIdea.setDescription(idea.getDescription());
        oldIdea.setAdvantages(idea.getAdvantages());
        if (oldIdea instanceof InternalIdea) {
            InternalIdea oldInternalIdea = (InternalIdea) oldIdea;
            InternalIdea internalIdea = (InternalIdea) idea;
            oldInternalIdea.setField(internalIdea.getField());
            return save(oldInternalIdea);
        } else {
            ProductIdea oldProductIdea = (ProductIdea) oldIdea;
            ProductIdea productIdea = (ProductIdea) idea;
            oldProductIdea.setTargetGroups(productIdea.getTargetGroups());
            oldProductIdea.setDistributionChannels(productIdea.getDistributionChannels());
            // Productline can only be updated if idea not submitted because when submitting a specialist is
            // assigned by productline. Also the internal productline can not be used for product ideas
            if (oldIdea.getStatus() == Status.NOT_SUBMITTED && !idea.getProductLine().getTitle().equals(getDefaultInternalProductLineTitle())) {
                oldIdea.setProductLine(idea.getProductLine());
            }
            return save(oldProductIdea);
        }
    }

    public Idea createByForm(Idea idea) throws InternalProductLineNotExistingException {
        try {
            if (idea instanceof InternalIdea) {
                idea.setProductLine(productLineService.findByTitle(getDefaultInternalProductLineTitle()).get(0));
            }
            idea.setCreator(userService.getCurrentUser());
            idea.setSpecialist(this.getSpecialistOfNewIdea(idea));
        } catch (UserNotFoundException e) {
            throw new NotAuthorizedException();
        }
        return save(idea);
    }

    public List<Idea> getSubmittedIdeas() {
        List<Idea> allIdeas = findAll();

        Predicate<Idea> ideaIsNotSubmitted = idea -> idea.getStatus().equals(Status.NOT_SUBMITTED);
        Predicate<Idea> ideaIsNotPublic = idea -> idea.getStatus().equals(Status.IDEA_STORAGE);

        return allIdeas.stream()
                .filter(Predicate.not(ideaIsNotSubmitted))
                .filter(Predicate.not(ideaIsNotPublic))
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
                return idea.getCreator().getId() == userService.getCurrentUser().getId();
            } catch (UserNotFoundException e) {
                throw new NotAuthorizedException();
            }
        };
        Predicate<Idea> ideaIsNotSubmitted = idea -> idea.getStatus().equals(Status.NOT_SUBMITTED);

        return allIdeas.stream().
                filter(ideaBelongsToCurUser.and(ideaIsNotSubmitted))
                .collect(Collectors.toList());
    }

    public Specialist getSpecialistOfNewIdea(Idea idea) throws UserNotFoundException, InternalProductLineNotExistingException {
        Optional<Specialist> specialist;
        if (idea instanceof ProductIdea) {
            specialist = getSpecialistOfNewProductlIdea((ProductIdea) idea);
            if (specialist.isEmpty()) {
                // if no matching specialist if found, return the first one you can find
                specialist = userService.findAllSpecialists().stream().findFirst();
            }
        } else {
            specialist = getSpecialistOfNewInternalIdea((InternalIdea) idea);
            if (specialist.isEmpty()) {
                throw new InternalProductLineNotExistingException();
            }
        }
        if (specialist.isEmpty()) {
            throw new UserNotFoundException();
        }
        return specialist.get();
    }


    private Optional<Specialist> getSpecialistOfNewInternalIdea(InternalIdea idea) {
        Optional<Specialist> specialist = Optional.empty();
        ProductLine internalProductLine =
                productLineService.findByTitle(getDefaultInternalProductLineTitle());
        if (!internalProductLines.isEmpty()) {
            specialist = getSpecialistsByProductLine(internalProductLine);
        }
        return specialist;
    }


    private Optional<Specialist> getSpecialistOfNewProductlIdea(ProductIdea idea) {
        Optional<Specialist> specialist = Optional.empty();
        ProductLine productLine = productLineService.findById(idea.getProductLine().getId());
        if (productLine != null) {
            specialist = getSpecialistsByProductLine(productLine);
        }
        return specialist;
    }

    private Optional<Specialist> getSpecialistsByProductLine(ProductLine productLine) {
        Optional<Specialist> specialist = Optional.empty();
        List<Specialist> specialists = userService.findSpecialistByProductLine_id(productLine.getId());
        if (!specialists.isEmpty()) {
            // when multiple specialists found, then return the one with least not submitted ideas (least work to do)
            specialists = specialists.stream().sorted(
                    Comparator.comparing(
                            s -> ideaRepository.countBySpecialist_idAndStatus(s.getId(), Status.NOT_SUBMITTED))).
                    collect(Collectors.toCollection(ArrayList::new));
            specialist = Optional.ofNullable(specialists.get(0));
        }
        return specialist;
    }

    public List<Idea> findBySpecialistIdAndStatus(Long specialist_id, Status status) {
        List<Idea> ideas;
        ideas = ideaRepository.findBySpecialistIdAndStatus(specialist_id, status);

        return ideas;
    }

    public List<Idea> findByStatus(Status status) {
        List<Idea> ideas;
        ideas = ideaRepository.findByStatus(status);

        return ideas;
    }

    public String getDefaultInternalProductLineTitle() {
        return DEFAULT_INTERNAL_PRODUCTLINE_TITLE;
    }

    public Idea saveDecision(Long id, Idea emptyIdeaWithDecision) {
        Idea persistedVersion = findById(id);
        //Idea needs to be pending and creator should be current editor
        if (persistedVersion.getCreator().equals(getUser()) && persistedVersion.getStatus().equals(Status.PENDING)) {
            //status should only be set to : Accepted, Declined, Idea_Storage
            if (emptyIdeaWithDecision.getStatus() != Status.PENDING && emptyIdeaWithDecision.getStatus() != Status.NOT_SUBMITTED) {
                persistedVersion.setStatus(emptyIdeaWithDecision.getStatus());
                persistedVersion.setStatusJustification(emptyIdeaWithDecision.getStatusJustification());
                return save(persistedVersion);
            } else throw new NotAuthorizedException();
        } else throw new NotAuthorizedException();
    }

    public Idea submitIdea(Idea idea) {
        Idea oldIdea = findById(idea.getId());
        try {
            if (oldIdea.getStatus().equals(Status.NOT_SUBMITTED) && userService.getCurrentUser().getId() == oldIdea.getCreator().getId()) {
                oldIdea.setStatus(Status.PENDING);
            } else {
                throw new NotAuthorizedException();
            }
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        return updateIdea(oldIdea, oldIdea.getId());
    }
}
