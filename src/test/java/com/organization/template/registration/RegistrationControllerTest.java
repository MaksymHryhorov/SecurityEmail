package com.organization.template.registration;

import com.organization.template.email.EmailSender;
import com.organization.template.registration.controller.RegistrationController;
import com.organization.template.registration.model.RegistrationRequest;
import com.organization.template.registration.service.RegistrationService;
import com.organization.template.user.model.User;
import com.organization.template.user.dao.UserRepository;
import com.organization.template.user.model.UserRole;
import com.organization.template.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Validated
@SpringBootTest
class RegistrationControllerTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailSender emailSender;

    @Autowired
    RegistrationService registrationService;

    @Autowired
    UserService userService;

    @Autowired
    RegistrationController registrationController;

    @Test
    void registerUser() {
        @Valid
        RegistrationRequest request = new RegistrationRequest(
                "TestName", "TestLast", "test@gmail.com", "pass");
        RegistrationRequest badRequest = new RegistrationRequest("", "", "", "");
        RegistrationRequest badRequest2 = new RegistrationRequest(
                "Correct", "Correct", "", "Correct");
        RegistrationRequest badRequest3 = new RegistrationRequest(
                "Correct", "Correct", "correct@gmail.com", "");
        RegistrationRequest badRequest4 = new RegistrationRequest(
                "\"", "Correct", "correct@gmail.com", "123456");

        User entity = userRepository.save(new User(
                request.getFirstName(), request.getLastName(),
                request.getPassword(), request.getEmail(), UserRole.USER));

        Optional<User> emailUser = userRepository.findByEmail(entity.getEmail());

        assertNotNull(entity);

        if (emailUser.isPresent()) {
            assertEquals(entity.getEmail(), emailUser.get().getEmail());
            assertEquals(entity.getFirstName(), emailUser.get().getFirstName());
            assertEquals(entity.getLastName(), emailUser.get().getLastName());
            assertEquals(entity.getPassword(), emailUser.get().getPassword());
        }

        assertThrows(ConstraintViolationException.class, () -> registrationController.register(badRequest));

        try {
            registrationController.register(badRequest2);
        } catch (ConstraintViolationException e) {
            assertEquals(
                    "register.request.email: Email must be between 5 and 100 characters", e.getMessage());
        }
        try {
            registrationController.register(badRequest3);
        } catch (ConstraintViolationException e) {
            assertEquals(
                    "register.request.password: Password must be between 6 and 50 characters", e.getMessage());
        }
        try {
            registrationController.register(badRequest4);
        } catch (ConstraintViolationException e) {
            assertEquals(
                    "register.request.firstName: Name must be between 2 and 50 characters", e.getMessage());
        }

    }

    @Test
    void sendConfirmationToMail() {
        RegistrationRequest request = new RegistrationRequest(
                "TestName", "TestLast", "testing@gmail.com", "pass");
        RegistrationRequest badRequest = new RegistrationRequest(
                "Correct", "Correct", "", "Correct");
        RegistrationRequest badRequest2 = new RegistrationRequest(
                "TestName", "TestLast",
                "testing@gmail.com", "pass");

        String token = userService.singUpUser(new User(
                request.getFirstName(), request.getLastName(),
                request.getPassword(), request.getEmail(), UserRole.USER));

        String link = "http://localhost:8080/product/registration/confirm?token=" + token;

        emailSender.send(request.getEmail(), registrationService.buildEmail(
                request.getFirstName() + " " + request.getLastName(), link));

        try {
            registrationController.register(badRequest);
        } catch (ConstraintViolationException e) {
            assertEquals(
                    "register.request.email: Email must be between 5 and 100 characters", e.getMessage());
        }

        assertThrows(IllegalStateException.class, () -> {
            String badToken = userService.singUpUser(new User(
                    badRequest2.getFirstName(), badRequest2.getLastName(),
                    badRequest2.getPassword(), badRequest2.getEmail(), UserRole.USER));
        });

    }

}