package de.fhdw.geiletypengmbh.digitalerbriefkasten.controller;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.NotAuthorizedException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.UserNotFoundException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.log.LogHelper;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.Specialist;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.User;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.Idea;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.Status;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.service.account.SecurityServiceImpl;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.service.account.UserServiceImpl;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.service.ideas.IdeaService;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    UserValidator userValidator;
    @Autowired
    IdeaService ideaService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private SecurityServiceImpl securityService;

    //Autor: JF
    @GetMapping("/registration")
    public String registration(Model model, @ModelAttribute("errors") ArrayList<String> errors) {
        model.addAttribute("userForm", new User());
        model.addAttribute("errors", errors);

        return "account/registration";
    }

    //Autor: PR
    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        //The Construction of this Controller as well as the whole password logic
        // was made with the assumption in mind of encripting password transmission trough https / SSL
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            ArrayList<String> errors = new ArrayList<>();
            bindingResult.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:/registration";
        }

        userService.save(userForm);

        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirmation());

        logger.info(String.format("[REGISTRATION] USERNAME: %s | IP: %s", userForm.getUsername(), LogHelper.getUserIpAddress()));

        return "redirect:/welcome";
    }

    //Autor: JF
    @GetMapping("/login")
    public String login(@ModelAttribute("userForm") User userForm, Model model, String logout) {
        //1.in regards to transmission of password in clear see registration()
        //2. multiple logins from same account concurrently are allowed -> possibility of multiple devices per user
        //Note that login Post Controller is provided automatically by Spring Security
        if (logout != null) {
            model.addAttribute("message", "Logout was successfull");
        }
        return "account/login";
    }

    //Autor: JF
    @GetMapping("/logout")
    public String redirectLogout(HttpServletRequest request, HttpServletResponse response) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.info(String.format("[LOGOUT] USERNAME: %s | IP: %s", auth.getName(), LogHelper.getUserIpAddress()));

        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return "redirect:/login?logout";
    }

    //Autor: JF
    @GetMapping({"/", "/welcome"})
    public String welcome(Model model, HttpServletRequest request) {
        //returns landing page -> if admin=admin else if specialist=specialist else welcome (user landing page)
        if (request.isUserInRole("ADMIN")) {
            return "account/admin";
        } else {
            return (request.isUserInRole("SPECIALIST") ? "redirect:/specialist" : "account/welcome");
        }
    }

    //Autor: JF
    @PreAuthorize("hasRole('ROLE_SPECIALIST')")
    @GetMapping("/specialist")
    public String specialist(Model model) throws UserNotFoundException {
        User user = userService.getCurrentUser();
        if (!(user instanceof Specialist)) {
            throw new NotAuthorizedException();
        }

        List<Idea> pendingIdeas = ideaService.findBySpecialistIdAndStatus(user.getId(), Status.PENDING);
        List<Idea> ideaStorageIdeas = ideaService.findByStatus(Status.IDEA_STORAGE);
        model.addAttribute("pendingIdeas", pendingIdeas);
        model.addAttribute("ideaStorageIdeas", ideaStorageIdeas);

        return "account/specialist";
    }
}


