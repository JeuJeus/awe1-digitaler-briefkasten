//Autor: PR
package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.account;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository<T extends User> extends JpaRepository<T, Long> {
    User findByUsername(String username);

    List<T> findAll();
}
