package de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.User;

public interface UserService {

    User save(User user);

    User findByUsername(String username);

    User findById(Long id);
}
