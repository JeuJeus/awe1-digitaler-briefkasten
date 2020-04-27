package de.fhdw.geiletypengmbh.digitalerbriefkasten.controller;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service.SecurityService;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service.SecurityServiceImpl;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service.UserServiceImpl;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.validator.UserValidator;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {

    @Autowired
    UserValidator userValidator;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private SecurityServiceImpl securityService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userService.save(userForm);

        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirmation());

        return "redirect:/welcome";
    }

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        //Note that login Post Controller is provided automatically by Spring Security
        if (error != null) {
            model.addAttribute("error", "Username and Password are invalid");
        }
        if (logout != null) {
            model.addAttribute("message", "Logout was successfull");
        }
        return "login";
    }

    @GetMapping({"/", "/welcome"})
    public String welcome(Model model, HttpServletRequest request) {
        //returns landing page -> if admin=admin else if specialist=specialist else welcome (user landing page)
        return (request.isUserInRole("ADMIN")) ? "admin" : (request.isUserInRole("SPECIALIST") ? "specialist" : "welcome");
    }
}
