package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByUsername(String username);
}
