package com.organization.template.registration;

import com.organization.template.email.EmailSender;
import com.organization.template.user.User;
import com.organization.template.user.UserRepository;
import com.organization.template.user.UserRole;
import com.organization.template.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    @Test
    void registerUser() {
        RegistrationRequest request = new RegistrationRequest(
                "TestName",
                "TestLast",
                "test@gmail.com",
                "pass"
        );

        RegistrationRequest badRequest = new RegistrationRequest(
                " ",
                "T",
                "test.com",
                ""
        );

        User entity = userRepository.save(new User(
                request.getFirstName(),
                request.getLastName(),
                request.getPassword(),
                request.getEmail(),
                UserRole.USER));

        User entity1 = userRepository.save(new User(
                badRequest.getFirstName(),
                badRequest.getLastName(),
                badRequest.getPassword(),
                badRequest.getEmail(),
                UserRole.USER));


        Optional<User> emailUser = userRepository.findByEmail(entity.getEmail());
        Optional<User> emailUser1 = userRepository.findByEmail(entity1.getEmail());

        assertNotNull(entity);
        assertNotNull(entity1);

        assertEquals(entity.getEmail(), emailUser.get().getEmail());
        assertEquals(entity.getUsername(), emailUser.get().getUsername());
        assertEquals(entity1.getUsername(), emailUser1.get().getUsername());

    }

    @Test
    void sendConfirmationToMail() {
        RegistrationRequest request = new RegistrationRequest(
                "TestName",
                "TestLast",
                "skelebob695@gmail.com",
                "pass"
        );
        String token = userService.singUpUser(new User(
                request.getFirstName(),
                request.getLastName(),
                request.getPassword(),
                request.getEmail(),
                UserRole.USER));

        String link = "http://localhost:8080/product/registration/confirm?token=" + token;

        emailSender.send(request.getEmail(), registrationService.buildEmail(request.getFirstName() + " " + request.getLastName(), link));

    }

}