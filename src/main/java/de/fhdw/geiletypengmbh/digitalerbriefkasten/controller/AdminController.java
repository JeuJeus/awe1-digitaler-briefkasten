package de.fhdw.geiletypengmbh.digitalerbriefkasten.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@Controller
public class AdminController {

    @GetMapping("/admin")
    public String adminHome() {
        return "admin";
    }
}
