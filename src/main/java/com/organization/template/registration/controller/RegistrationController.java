package com.organization.template.registration.controller;

import com.organization.template.registration.model.RegistrationRequest;
import com.organization.template.registration.service.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "product/registration")
@AllArgsConstructor
@Validated
public class RegistrationController {
    private final RegistrationService registrationService;

    /**
     * Register new User
     *
     * @param request (firstName, lastName, password, email, role)
     * @return user token
     */
    @PostMapping
    public String register(@Valid @RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }

    /**
     * Confirm user token
     *
     * @param token user
     * @return confirmation page
     */
    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
}
