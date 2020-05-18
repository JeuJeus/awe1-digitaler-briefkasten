//Autor: JF
package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.account;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
