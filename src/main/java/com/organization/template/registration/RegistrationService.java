package com.organization.template.registration;

import com.organization.template.user.User;
import com.organization.template.user.UserRole;
import com.organization.template.user.UserService;
import com.organization.template.email.EmailSender;
import com.organization.template.registration.token.ConfirmationToken;
import com.organization.template.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;

    /**
     * Get json request with params and register new User
     *
     * @param request
     * @return token authentication
     */
    public String register(RegistrationRequest request) {
        String token = userService.singUpUser(
                new User(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getPassword(),
                        request.getEmail(),
                        UserRole.USER)
        );

        // authentication link
        String link = "http://localhost:8080/product/registration/confirm?token=" + token;

        // send authentication link to mail
        emailSender.send(request.getEmail(), buildEmail(request.getFirstName() + " " + request.getLastName(), link));

        return token;
    }

    /**
     * Check if token: not found, already confirmed or expired.
     * Update confirmed time, update user enable
     *
     * @param token
     * @return confirmed page
     */
    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
                .orElseThrow(() -> new IllegalStateException("Token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiredAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        userService.enableAppUser(confirmationToken.getUser().getEmail());

        return "Account confirmed";
    }

    /**
     * Get html template file and validate name and link
     *
     * @param name (firstName, lastName)
     * @param link (confirmation token link)
     * @return html file with letter template
     */
    @SneakyThrows
    private String buildEmail(String name, String link) {
        Path filePath = Path.of("D:\\Projects\\template\\src\\main\\resources\\templates\\email.html");

        String content = Files.readString(filePath);
        String validateLink = content.replace("{link}", link);
        String validateName = validateLink.replace("{name}", name);

        return validateName;
    }

}
