package com.example.reddit.service;

import java.time.Instant;
import java.util.UUID;

import com.example.reddit.dto.RegisterRequest;
import com.example.reddit.exception.SpringRedditException;
import com.example.reddit.model.NotificationEmail;
import com.example.reddit.model.User;
import com.example.reddit.model.VerificationToken;
import com.example.reddit.repository.UserRepository;
import com.example.reddit.repository.VerificationTokenRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;

    @Transactional
    public void signup(RegisterRequest registerRequest) {
        User user = new User();

        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        String token = generateVerificationToken(user);

        NotificationEmail notificationEmail = new NotificationEmail();

        notificationEmail.setSubject("Please activate your account");
        notificationEmail.setRecipient(user.getEmail());
        notificationEmail.setMessage("Thank you for signing up to Spring Reddit, " +
                "please click on the below url to activate your account : ");
        notificationEmail.setLink("http://localhost:8080/api/auth/verifyAccount/" + token);

        mailService.sendMail(notificationEmail);
    }

    @Transactional
    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken();

        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);

        return token;
    }

    public void verifyAccount(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new SpringRedditException("Invalid Token"));

        fetchUserAndEnable(verificationToken);
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new SpringRedditException("User not found with username -" + username));

        user.setEnabled(true);

        userRepository.save(user);
    }
}
