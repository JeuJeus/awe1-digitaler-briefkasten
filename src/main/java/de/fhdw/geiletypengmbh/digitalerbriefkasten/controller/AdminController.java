package de.fhdw.geiletypengmbh.digitalerbriefkasten.controller;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.User;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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

        ModelAndView mav = new ModelAndView("admin");
        mav.addObject("userList", userList);

        return mav;
    }
}
