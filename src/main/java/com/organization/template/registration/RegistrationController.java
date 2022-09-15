package com.organization.template.registration;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "product/registration")
@AllArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    /**
     * Register new User
     * @param request (firstName, lastName, password, email, role)
     * @return user token
     */
    @PostMapping
    public String register(@RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }

    /**
     * Confirm user token
     * @param token user
     * @return confirmation page
     */
    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
}
