package com.organization.template.registration;

import com.organization.template.appuser.User;
import com.organization.template.appuser.UserRole;
import com.organization.template.appuser.UserService;
import com.organization.template.registration.token.ConfirmationToken;
import com.organization.template.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserService userService;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());

        if (!isValidEmail) {
            throw new IllegalStateException("Email is not valid");
        }

        String token = userService.singUpUser(
                new User(
                request.getFirstName(),
                request.getLastName(),
                request.getPassword(),
                request.getEmail(),
                UserRole.USER)
        );

        return token;
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
                .orElseThrow(() -> new IllegalStateException("Token not found"));

        /*if (confirmationToken.getCreatedAt() != null) {
            throw new IllegalStateException("Email already confirmed");
        }*/

        LocalDateTime expiredAt = confirmationToken.getExpiredAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        userService.enableAppUser(confirmationToken.getUser().getEmail());

        return "confirmed";
    }

}
