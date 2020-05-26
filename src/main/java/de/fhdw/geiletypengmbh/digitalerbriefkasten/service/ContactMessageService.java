//Autor: PR
package de.fhdw.geiletypengmbh.digitalerbriefkasten.service;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.ContactMessageNotFoundException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.NotAValidMailException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.UserNotFoundException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ContactMessage;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.ContactMessageRepository;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.service.account.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

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

    public List<ContactMessage> findAllNotAnswered() {
        return contactMessageRepository.findAllByAnswered(false);
    }

    //Autor: JF
    public ContactMessage save(ContactMessage contactMessage) {
        try {
            return contactMessageRepository.save(contactMessage);
        } catch (TransactionSystemException email) {
            //Only Reason this could be thrown is because Email Attribute
            //when not a rfc compliant mail
            throw new NotAValidMailException();
        }
    }

    public ContactMessage saveByUI(ContactMessage contactMessage) {
        try {
            contactMessage.setUser(userService.getCurrentUser());
        } catch (UserNotFoundException ignored) {
            // Unregistered users are allowed
        }
        return save(contactMessage);
    }

    public ContactMessage setToAnswered(Long id) {
        ContactMessage contactMessage = findById(id);
        contactMessage.setAnswered(true);
        return save(contactMessage);
    }
}
