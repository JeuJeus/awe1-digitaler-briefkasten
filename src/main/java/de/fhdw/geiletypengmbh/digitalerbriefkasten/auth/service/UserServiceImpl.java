package de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions.UserNotFoundException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.Specialist;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.User;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository<User> userRepository;

    @Autowired
    private UserRepository<Specialist> specialistRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        if (user.getRoles() == null) {
            //this ensures by default specialist/user gets set the "SPECIALIST/"USER" role
            if (user instanceof Specialist) user.setRoles(roleService.secureSupplyOfProvidedRole("SPECIALIST"));
            else user.setRoles(roleService.secureSupplyOfProvidedRole("USER"));
        }
        if (user instanceof Specialist) return specialistRepository.saveAndFlush(user);
        else return userRepository.saveAndFlush(user);
    }

    @Override
    public User findByUsername(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            user = specialistRepository.findByUsername(username);
            if (user == null) {
                throw new UserNotFoundException();
            }
        }
        return user;
    }

    @Override
    public User findById(Long id) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            user = specialistRepository.findById(id);
            if (user.isEmpty()) {
                throw new UserNotFoundException();
            }
        }
        return user.orElseThrow(UserNotFoundException::new);
    }
}
