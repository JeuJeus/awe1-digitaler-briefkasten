package de.fhdw.geiletypengmbh.digitalerbriefkasten.controller;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.UserNotFoundException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ContactMessage;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.Specialist;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.User;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.DistributionChannel;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.Field;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.ProductLine;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.TargetGroup;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.service.ContactMessageService;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.service.account.UserServiceImpl;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.service.ideas.*;
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

    public static final String ERRORS = "errors";
    public static final String SUCCESS = "success";
    public static final String REDIRECT_ADMIN = "redirect:/admin";
    @Autowired
    UserValidator userValidator;
    @Autowired
    IdeaService ideaService;
    @Autowired
    FieldService fieldService;
    @Autowired
    TargetGroupService targetGroupService;
    @Autowired
    DistributionChannelService distributionChannelService;
    @Autowired
    ProductLineService productLineService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private ContactMessageService contactMessageService;

    //Autor: JF
    @GetMapping("/admin")
    public ModelAndView adminPanel() {

        List<User> userList = userService.findAll();
        List<ProductLine> productLines = productLineService.findAll();
        List<ContactMessage> contactMessages = contactMessageService.findAllNotAnswered();

        ModelAndView mav = new ModelAndView("account/admin");
        mav.addObject("userList", userList);
        mav.addObject("productLines", productLines);
        mav.addObject("contactMessages", contactMessages);

        return mav;
    }

    //Autor: JF
    @GetMapping("/admin/userDetails/{username}")
    public ModelAndView listUserDetails(@PathVariable String username) throws UserNotFoundException {
        User user = userService.findByUsername(username);

        ModelAndView mav = new ModelAndView("account/userDetails");
        mav.addObject("user", user);

        return mav;
    }

    //Autor: JB
    @PostMapping("/admin/createSpecialist")
    public String registration(@ModelAttribute("userForm") Specialist userForm, BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        userValidator.validateSpecialist(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            ArrayList<String> errors = new ArrayList<>();
            bindingResult.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
            redirectAttributes.addFlashAttribute(ERRORS, errors);
            return REDIRECT_ADMIN;
        }

        userService.save(userForm);
        redirectAttributes.addFlashAttribute(SUCCESS, "Spezialist erfolgreich angelegt.");
        return REDIRECT_ADMIN;
    }

    //Autor: JB
    @PostMapping("/admin/createProductLine")
    public String createProductLine(@ModelAttribute ProductLine productLine, RedirectAttributes redirectAttributes) {

        try {
            productLineService.save(productLine);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ERRORS, e.getMessage());
            return REDIRECT_ADMIN;
        }
        redirectAttributes.addFlashAttribute(SUCCESS, "Produktlinie erfolgreich angelegt.");
        return REDIRECT_ADMIN;
    }

    //Autor: JB
    @PostMapping("/admin/createField")
    public String createField(@ModelAttribute Field field, RedirectAttributes redirectAttributes) {

        try {
            fieldService.save(field);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ERRORS, e.getMessage());
            return REDIRECT_ADMIN;
        }
        redirectAttributes.addFlashAttribute(SUCCESS, "Handlungsfeld erfolgreich angelegt.");
        return REDIRECT_ADMIN;
    }

    //Autor: JB
    @PostMapping("/admin/createTargetGroup")
    public String createTargetGroup(@ModelAttribute TargetGroup targetGroup, RedirectAttributes redirectAttributes) {

        try {
            targetGroupService.save(targetGroup);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ERRORS, e.getMessage());
            return REDIRECT_ADMIN;
        }
        redirectAttributes.addFlashAttribute(SUCCESS, "Zielgruppe erfolgreich angelegt.");
        return REDIRECT_ADMIN;
    }

    //Autor: JB
    @PostMapping("/admin/createDistributionChannel")
    public String createDistributionChannel(@ModelAttribute DistributionChannel distributionChannel,
                                            RedirectAttributes redirectAttributes) {

        try {
            distributionChannelService.save(distributionChannel);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ERRORS, e.getMessage());
            return REDIRECT_ADMIN;
        }
        redirectAttributes.addFlashAttribute(SUCCESS, "Vertriebskanal erfolgreich angelegt.");
        return REDIRECT_ADMIN;
    }
}
