package de.fhdw.geiletypengmbh.digitalerbriefkasten.controller;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.Specialist;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.User;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.ProductLine;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.account.UserRepository;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.ideas.ProductLineRepository;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.service.account.UserServiceImpl;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.service.ideas.IdeaService;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@Controller
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductLineRepository productLineRepository;

    @Autowired
    UserValidator userValidator;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    IdeaService ideaService;

    @GetMapping("/admin")
    public ModelAndView adminPanel() {

        List<User> userList = userRepository.findAll();
        List<ProductLine> productLines = productLineRepository.findAll();

        ModelAndView mav = new ModelAndView("account/admin");
        mav.addObject("userList", userList);
        mav.addObject("productLines", productLines);

        return mav;
    }

    @GetMapping("/admin/userDetails/{username}")
    public ModelAndView listUserDetails(@PathVariable String username) {
        User user = userRepository.findByUsername(username);

        ModelAndView mav = new ModelAndView("account/userDetails");
        mav.addObject("user", user);

        return mav;
    }

    @PostMapping("/admin/createSpecialist")
    public String registration(@ModelAttribute("userForm") Specialist userForm, BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        userValidator.validateSpecialist(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            ArrayList<String> errors = new ArrayList<>();
            bindingResult.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:/admin?failure";
        }

        userService.save(userForm);

        return "redirect:/admin?success";
    }
}
