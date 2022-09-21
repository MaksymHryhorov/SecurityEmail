package com.organization.template.user.service;

import com.organization.template.registration.token.model.ConfirmationToken;
import com.organization.template.registration.token.service.ConfirmationTokenService;
import com.organization.template.user.dao.UserRepository;
import com.organization.template.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private static final String USER_NOT_FIND_MSG = "User with email %s not found";

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    /**
     * Check if user email not found
     * @param email
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FIND_MSG, email)));
    }

    /**
     * Check does email present.
     * Encrypt a password, generate token and save to the DB.
     * Save token creation time. Activate time 15 minutes.
     *
     * @param user
     * @return user token
     */
    public String singUpUser(User user) {
        boolean userExists = userRepository.findByEmail(user.getEmail())
                .isPresent();

        if (userExists) {
            throw new IllegalStateException("Email already exists");
        }

        String encodingPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodingPassword);

        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15), user);

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

    /**
     * Change user enable status
     *
     * @param email
     * @return
     */
    public int enableAppUser(String email) {
        return userRepository.enableAppUser(email);
    }

}
