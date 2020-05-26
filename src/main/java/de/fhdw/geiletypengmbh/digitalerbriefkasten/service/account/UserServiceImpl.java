package de.fhdw.geiletypengmbh.digitalerbriefkasten.service.account;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.UserNotFoundException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.Specialist;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.User;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.account.SpecialistRepository;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.account.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository<User> userRepository;

    @Autowired
    private SpecialistRepository specialistRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //Autor: JF
    @Override
    public User save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        if (user.getRoles() == null) {
            //this ensures by default specialist/user gets set the "SPECIALIST/"USER" role
            if (user instanceof Specialist) user.setRoles(roleService.secureSupplyOfProvidedRole("SPECIALIST"));
            else user.setRoles(roleService.secureSupplyOfProvidedRole("USER"));
        }
        return userRepository.saveAndFlush(user);
    }

    //Autor: PR
    @Override
    public User findByUsername(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) throw new UserNotFoundException();
        return user;
    }

    //Autor: PR
    @Override
    public User findById(Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    //Autor: PR
    public List<Specialist> findAllSpecialists() {
        return specialistRepository.findAll();
    }

    //Autor: PR
    public List<Specialist> findSpecialistByproductLineId(Long productLineId) {
        return specialistRepository.findByProductLinesId(productLineId);
    }

    //Autor: JF
    public User getCurrentUser() throws UserNotFoundException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal == "anonymousUser") return null;
        String username = ((UserDetails) principal).getUsername();
        return this.findByUsername(username);
    }

    //Autor: JF
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
