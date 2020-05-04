package de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.Role;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.account.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

import static java.util.Collections.singleton;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Set<Role> secureSupplyOfProvidedRole(String roleName) {
        Role toProvide = roleRepository.findByName(roleName);
        if (toProvide == null) {
            Role userRole = new Role();
            userRole.setName(roleName);

            roleRepository.saveAndFlush(userRole);

            return singleton(userRole);
        } else return singleton(toProvide);
    }
}
