package de.fhdw.geiletypengmbh.digitalerbriefkasten.controller;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ContactMessage;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.service.ContactMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ContactMessageController {

    @Autowired
    ContactMessageService contactMessageService;

    @PostMapping("/contact")
    public String saveContactMessage(@ModelAttribute ContactMessage contactMessage) {
        contactMessageService.saveByUI(contactMessage);
        return "redirect:/welcome";
    }


}


