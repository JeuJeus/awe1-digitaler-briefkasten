//Autor: PR
package de.fhdw.geiletypengmbh.digitalerbriefkasten.service;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.ContactMessageNotFoundException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.NotAuthorizedException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.UserNotFoundException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ContactMessage;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.ContactMessageRepository;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.service.account.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactMessageService {

    @Autowired
    private ContactMessageRepository contactMessageRepository;

    @Autowired
    private UserServiceImpl userService;

    public ContactMessage findById(Long id) {
        return contactMessageRepository.findById(id).orElseThrow(ContactMessageNotFoundException::new);
    }

    public List<ContactMessage> findAll() {
        return contactMessageRepository.findAll();
    }

    public ContactMessage save(ContactMessage contactMessage) {
        return contactMessageRepository.save(contactMessage);
    }

    public ContactMessage saveByUI(ContactMessage contactMessage) {
        try {
            contactMessage.setUser(userService.getCurrentUser());
        } catch (UserNotFoundException e) {
            throw new NotAuthorizedException();
        }
        return save(contactMessage);
    }
}
