package com.organization.template.registration;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidatorException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        }));

        return errors;
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
