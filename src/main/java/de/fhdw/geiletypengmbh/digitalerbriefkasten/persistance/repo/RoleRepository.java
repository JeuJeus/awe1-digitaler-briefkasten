package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
}
