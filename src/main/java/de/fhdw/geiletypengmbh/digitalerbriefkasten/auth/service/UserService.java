package de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.User;

public interface UserService {

    void save(User user);

    User findByUsername(String username);
}
