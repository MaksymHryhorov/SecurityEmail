package com.organization.template.registration.token;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenServiceRepository confirmationTokenServiceRepository;

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenServiceRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenServiceRepository.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return confirmationTokenServiceRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }

}
