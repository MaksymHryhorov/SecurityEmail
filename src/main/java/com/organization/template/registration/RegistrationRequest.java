package com.organization.template.registration;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequest {

    @NotNull(message = "First Name can not be null")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private final String firstName;

    @NotNull(message = "Last Name can not be null")
    @Size(min = 2, max = 50, message = "Last Name must be between 2 and 50 characters")
    private final String lastName;

    @NotNull(message = "Email can not be null")
    @Size(min = 5, max = 100, message = "Email must be between 5 and 100 characters")
    @Email
    private final String email;

    @NotNull(message = "Password can not be null")
    @Size(min = 6, max = 50, message = "Password must be between 6 and 50 characters")
    private final String password;
}
