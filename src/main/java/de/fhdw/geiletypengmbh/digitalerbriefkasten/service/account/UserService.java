//Autor: JF
package de.fhdw.geiletypengmbh.digitalerbriefkasten.service.account;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.UserNotFoundException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.User;

public interface UserService {

    User save(User user);

    User findByUsername(String username) throws UserNotFoundException;

    User findById(Long id) throws UserNotFoundException;
}
