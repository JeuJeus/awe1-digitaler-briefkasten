package de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.Role;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

import static java.util.Collections.singleton;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Set<Role> secureSupplyOfUserRole() {
        Role user = roleRepository.findByName("USER");
        if (user == null) {
            Role userRole = new Role();
            userRole.setName("USER");

            roleRepository.saveAndFlush(userRole);

            return singleton(userRole);
        } else return singleton(user);
    }
}
