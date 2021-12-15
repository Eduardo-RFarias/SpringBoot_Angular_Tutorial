package edu.unb.reddit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.unb.reddit.dto.AuthenticationResponse;
import edu.unb.reddit.dto.LoginRequest;
import edu.unb.reddit.dto.RegisterRequest;
import edu.unb.reddit.service.AuthService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/signup/")
	public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
		authService.signup(registerRequest);

		return ResponseEntity.ok().body("User Registration Successful");
	}

	@GetMapping("/verifyAccount/{token}/")
	public ResponseEntity<String> verifyAccount(@PathVariable String token) {
		authService.verifyAccount(token);

		return ResponseEntity.ok().body("Account verified Successfully");
	}

	@PostMapping("/login/")
	public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
		var response = authService.login(loginRequest);

		return ResponseEntity.ok().body(response);
	}

}
