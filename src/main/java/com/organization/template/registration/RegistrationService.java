package com.organization.template.registration;

import com.organization.template.appuser.User;
import com.organization.template.appuser.UserRole;
import com.organization.template.appuser.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserService userService;
    private final EmailValidator emailValidator;

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());

        if (!isValidEmail) {
            throw new IllegalStateException("Email is not valid");
        }

        return userService.singUpUser(new User(
                request.getFirstName(),
                request.getLastName(),
                request.getPassword(),
                request.getEmail(),
                UserRole.USER));
    }

}
