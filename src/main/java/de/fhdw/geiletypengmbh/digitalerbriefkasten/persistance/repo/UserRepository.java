package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.User;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.Idea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository<T extends User> extends JpaRepository<User, Long> {
    User findByUsername(String username);

    List<User> findAll();
}
