package de.fhdw.geiletypengmbh.digitalerbriefkasten.validator;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.UserNotFoundException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.Specialist;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.User;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.service.account.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    public static final String USERNAME = "username";
    public static final String NOT_EMPTY = "NotEmpty";
    public static final String PASSWORD = "password";
    @Autowired
    private UserServiceImpl userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, USERNAME, NOT_EMPTY, "Username is empty");
        if (user.getUsername().length() < 3 || user.getUsername().length() > 32) {
            //Implemented here for practical value -> "max"=ok | "Äteritsiputeritsipuolilautatsijänkä"!=ok (place in finland)
            errors.rejectValue(USERNAME, "Size.userForm.username", "Username is too short." +
                    "\n (Should be at least 4 characters long.)");
        }
        try {
            if (userService.findByUsername(user.getUsername()) != null) {
                errors.rejectValue(USERNAME, "Duplicate.userForm.username", "Username already exists");
            }
        } catch (UserNotFoundException e) {
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, PASSWORD, NOT_EMPTY, "Password is empty");
        if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
            errors.rejectValue(PASSWORD, "Size.userForm.password", "Password is too short." +
                    "\n (Should be at least 3 characters.)");
        }
        if (!user.getPasswordConfirmation().equals(user.getPassword())) {
            errors.rejectValue("passwordConfirmation", "Diff.userForm.passwordConfirmation", "Passwords do not match.");
        }
    }

    public void validateSpecialist(Object o, Errors errors) {
        //TOD DUPLICATE BLOCK OF CODE ->  REFACTOR? -> JONATHAN
        Specialist specialist = (Specialist) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, USERNAME, NOT_EMPTY, "Username is empty");
        if (specialist.getUsername().length() < 3 || specialist.getUsername().length() > 32) {
            //Implemented here for practical value -> "max"=ok | "Äteritsiputeritsipuolilautatsijänkä"!=ok (place in finland)
            errors.rejectValue(USERNAME, "Size.userForm.username", "Username is too short." +
                    "\n (Should be at least 4 characters long.)");
        }
        try {
            if (userService.findByUsername(specialist.getUsername()) != null) {
                errors.rejectValue(USERNAME, "Duplicate.userForm.username", "Username already exists");
            }
        } catch (UserNotFoundException e) {
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, PASSWORD, NOT_EMPTY, "Password is empty");
        if (specialist.getPassword().length() < 8 || specialist.getPassword().length() > 32) {
            errors.rejectValue(PASSWORD, "Size.userForm.password", "Password is too short." +
                    "\n (Should be at least 3 characters.)");
        }
    }
}
