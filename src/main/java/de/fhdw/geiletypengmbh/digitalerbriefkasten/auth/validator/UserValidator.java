package de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.validator;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service.UserServiceImpl;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions.UserNotFoundException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Autowired
    private UserServiceImpl userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty", "Username is empty");
        if (user.getUsername().length() < 3 || user.getUsername().length() > 32) {
            //Implemented here for practical value -> "max"=ok | "Äteritsiputeritsipuolilautatsijänkä"!=ok (place in finland)
            errors.rejectValue("username", "Size.userForm.username", "Username is too short." +
                    "\n (Should be at least 4 characters long.)");
        }
        try {
            if (userService.findByUsername(user.getUsername()) != null) {
                errors.rejectValue("username", "Duplicate.userForm.username", "Username already exists");
            }
        } catch (UserNotFoundException e) {
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty", "Password is empty");
        if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.userForm.password", "Password is too short." +
                    "\n (Should be at least 3 characters.)");
        }
        if (!user.getPasswordConfirmation().equals(user.getPassword())) {
            errors.rejectValue("passwordConfirmation", "Diff.userForm.passwordConfirmation", "Passwords do not match.");
        }
    }
}
