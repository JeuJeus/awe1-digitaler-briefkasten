package de.fhdw.geiletypengmbh.digitalerbriefkasten.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomepageController {
    @Value("${spring.application.name}")
    String appName;

    @GetMapping("/test")
    public String homePage(Model model){
        //TODO to be removed -> set home somewhere
        model.addAttribute("appName", appName);
        return "home";
    }
}
