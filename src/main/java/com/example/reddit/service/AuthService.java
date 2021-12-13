package com.example.reddit.service;

import java.time.Instant;
import java.util.UUID;

import com.example.reddit.dto.AuthenticationResponse;
import com.example.reddit.dto.LoginRequest;
import com.example.reddit.dto.RegisterRequest;
import com.example.reddit.exception.SpringRedditException;
import com.example.reddit.model.NotificationEmail;
import com.example.reddit.model.RedditUser;
import com.example.reddit.model.VerificationToken;
import com.example.reddit.repository.UserRepository;
import com.example.reddit.repository.VerificationTokenRepository;
import com.example.reddit.security.JwtProvider;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Transactional
    public void signup(RegisterRequest registerRequest) {
        RedditUser user = new RedditUser();

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
    private String generateVerificationToken(RedditUser user) {
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
        RedditUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new SpringRedditException("User not found with username -" + username));

        user.setEnabled(true);

        userRepository.save(user);
    }

    @Transactional
    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        return new AuthenticationResponse(token, loginRequest.getUsername());
    }
}
