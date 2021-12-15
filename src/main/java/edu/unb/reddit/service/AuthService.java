package edu.unb.reddit.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.unb.reddit.dto.AuthenticationResponse;
import edu.unb.reddit.dto.LoginRequest;
import edu.unb.reddit.dto.RegisterRequest;
import edu.unb.reddit.exception.InvalidTokenException;
import edu.unb.reddit.exception.NotFoundException;
import edu.unb.reddit.model.NotificationEmail;
import edu.unb.reddit.model.RedditUser;
import edu.unb.reddit.model.VerificationToken;
import edu.unb.reddit.repository.UserRepository;
import edu.unb.reddit.repository.VerificationTokenRepository;
import edu.unb.reddit.security.JwtProvider;
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
		var user = new RedditUser();

		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setCreated(Instant.now());
		user.setEnabled(false);

		userRepository.save(user);

		var token = generateVerificationToken(user);

		var notificationEmail = new NotificationEmail();

		notificationEmail.setSubject("Please activate your account");
		notificationEmail.setRecipient(user.getEmail());
		notificationEmail.setMessage("Thank you for signing up to Spring Reddit, "
				+ "please click on the below url to activate your account : ");
		notificationEmail.setLink("http://localhost:8080/api/auth/verifyAccount/" + token);

		mailService.sendMail(notificationEmail);
	}

	@Transactional
	private String generateVerificationToken(RedditUser user) {
		var token = UUID.randomUUID().toString();

		var verificationToken = new VerificationToken();

		verificationToken.setToken(token);
		verificationToken.setUser(user);

		verificationTokenRepository.save(verificationToken);

		return token;
	}

	@Transactional
	public void verifyAccount(String token) {
		var verificationToken = verificationTokenRepository.findByToken(token)
				.orElseThrow(() -> new InvalidTokenException("Given validation Token is invalid"));

		fetchUserAndEnable(verificationToken);
	}

	@Transactional
	private void fetchUserAndEnable(VerificationToken verificationToken) {
		var username = verificationToken.getUser().getUsername();
		var user = userRepository.findByUsername(username)
				.orElseThrow(() -> new NotFoundException("User not found with username -" + username));

		user.setEnabled(true);

		userRepository.save(user);
	}

	@Transactional
	public AuthenticationResponse login(LoginRequest loginRequest) {
		var authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		var token = jwtProvider.generateToken(authentication);

		return new AuthenticationResponse(token, loginRequest.getUsername());
	}
}
