package de.fhdw.geiletypengmbh.digitalerbriefkasten.service.ideas;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.*;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.Role;
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

    private final String DEFAULT_INTERNAL_PRODUCTLINE_TITLE = "INTERNAL";

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
        Idea idea = ideaRepository.findById(id).orElseThrow(IdeaNotFoundException::new);
        // Only subtypes of Idea should be found
        if (!Idea.class.isAssignableFrom(idea.getClass())) {
            throw new IdeaNotFoundException();
        }
        assureIdeaAccessRights(idea);
        return idea;
    }

        private void assureIdeaAccessRights(Idea idea) {
        User currentUser = null;
        try {
            currentUser = userService.getCurrentUser();
        } catch (UserNotFoundException e) {
            currentUser = null;
        }
        //TODO MAYBE REFACTOR ME -> Jonathan
        if(idea.getStatus().equals(Status.NOT_SUBMITTED) && !idea.getCreator().getUsername().equals(currentUser.getUsername())){
            //not submitted ideas should only available for their creator
            throw new NotAuthorizedException();
        }
        else if(idea.getStatus().equals(Status.IDEA_STORAGE) && !currentUser.getRoles().equals("SPECIALIST")){
            //ideas in idea Storage should only be accessible to specialists
            throw new NotAuthorizedException();
        }
        else if(!idea.getCreator().getUsername().equals(currentUser.getUsername()) && !idea.getSpecialist().equals(currentUser.getUsername())){
            //admin has "godpower" and can view every idea
            if(!currentUser.getRoles().equals("ADMIN")){
                throw new NotAuthorizedException();
            }
        }
    }

    public Idea save(Idea idea) {
        //Only logged in Users are able to create Ideas
        try {
            userService.getCurrentUser();
            return ideaRepository.saveAndFlush(idea);
        } catch (UserNotFoundException e) {
            throw new NotAuthorizedException();
        } catch (DataIntegrityViolationException e){
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
        //TODO CHECK IF IDEA HAS CHANGED IN MEANTIME -> PHILIPP
        //eventually throws IdeaNotFoundException, thats why its here :)
        Idea checkExistantIdea = this.findById(id);
        return ideaRepository.saveAndFlush(idea);
    }

    public Idea createByForm(Idea idea) throws InternalProductLineNotExistingException {
        try {
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

    //TODO MAYBE CHANGE LOGIC TO: WHEN MULTIPLE SPECIALISTS FOUND, PICK THE ONE WITH LEAST PENDING IDEAS -> PHULLIPEH
    private Optional<Specialist> getSpecialistOfNewInternalIdea(InternalIdea idea) {
        Optional<Specialist> specialist = Optional.empty();
        List<ProductLine> internalProductLines =
                productLineService.findByTitle(getDefaultInternalProductLineTitle());
        if (!internalProductLines.isEmpty()) {
            ProductLine internalProductLine = internalProductLines.get(0);
            List<Specialist> internalSpecialists = userService.findSpecialistByProductLine_id(internalProductLine.getId());
            if (!internalSpecialists.isEmpty()) {
                specialist = Optional.ofNullable(internalSpecialists.get(0));
            }
        }
        return specialist;
    }

    //TODO MAYBE CHANGE LOGIC TO: WHEN MULTIPLE SPECIALISTS FOUND, PICK RANDOM ONE? -> PHULLIPEH
    private Optional<Specialist> getSpecialistOfNewProductlIdea(ProductIdea idea) {
        Optional<Specialist> specialist = Optional.empty();
        ProductLine productLine = productLineService.findById(idea.getProductLine().getId());
        if (productLine != null) {
            List<Specialist> specialists = userService.findSpecialistByProductLine_id(productLine.getId());
            if (!specialists.isEmpty()) {
                specialist = Optional.ofNullable(specialists.get(0));
            }
        }
        return specialist;
    }

    public List<Idea> findBySpecialistIdAndStatus(Long specialist_id, Status status) {
        List<Idea> ideas = new ArrayList<>();
        ideas = ideaRepository.findBySpecialistIdAndStatus(specialist_id, status);

        return ideas;
    }

    public List<Idea> findByStatus(Status status){
        List<Idea> ideas = new ArrayList<>();
        ideas = ideaRepository.findByStatus(status);

        return ideas;
    }

    public String getDefaultInternalProductLineTitle() {
        return DEFAULT_INTERNAL_PRODUCTLINE_TITLE;
    }

    public Idea saveDecision(Long id, Idea emptyIdeaWithDecision) {
        if(emptyIdeaWithDecision.getStatus() != Status.PENDING && emptyIdeaWithDecision.getStatus() != Status.NOT_SUBMITTED){
            Idea updateDecision = findById(id);
            updateDecision.setStatus(emptyIdeaWithDecision.getStatus());
            updateDecision.setStatusJustification(emptyIdeaWithDecision.getStatusJustification());
            return save(updateDecision);
        }else throw new NotAuthorizedException();
    }

    public Idea saveUpdateIdea(Long id, Idea idea) {
        Idea oldIdea = findById(id);
        oldIdea.setDescription(idea.getDescription());
        oldIdea.setProductLine(idea.getProductLine());
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
            System.out.println(productIdea.toString());
            return save(oldProductIdea);
        }
    }
}
