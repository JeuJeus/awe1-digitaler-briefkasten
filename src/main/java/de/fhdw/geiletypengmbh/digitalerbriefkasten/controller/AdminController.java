package de.fhdw.geiletypengmbh.digitalerbriefkasten.controller;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.User;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.account.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@Controller
public class AdminController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/admin")
    public ModelAndView adminPanel() {

        List<User> userList = userRepository.findAll();

        ModelAndView mav = new ModelAndView("account/admin");
        mav.addObject("userList", userList);
        mav.addObject("specialist", false);

        return mav;
    }

    @GetMapping("/admin/userDetails/{username}")
    public ModelAndView listUserDetails(@PathVariable String username) {
        User user = userRepository.findByUsername(username);

        ModelAndView mav = new ModelAndView("account/userDetails");
        mav.addObject("user", user);
        mav.addObject("specialist", false);

        return mav;
    }
}
