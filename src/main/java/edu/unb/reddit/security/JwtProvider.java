package edu.unb.reddit.security;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.annotation.PostConstruct;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import edu.unb.reddit.exception.UnknownException;
import io.jsonwebtoken.Jwts;

@Service
public class JwtProvider {
	private KeyStore keyStore;

	@PostConstruct
	public void init() {
		try {
			keyStore = KeyStore.getInstance("JKS");
			var resourceAsStream = getClass().getResourceAsStream("/springblog.jks");
			keyStore.load(resourceAsStream, "secret".toCharArray());
		} catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
			throw new UnknownException("Exception occurred while loading keystore");
		}
	}

	public String generateToken(Authentication authentication) {
		var user = (User) authentication.getPrincipal();

		return Jwts.builder().setSubject(user.getUsername()).signWith(getPrivateKey()).compact();
	}

	public boolean validateToken(String jwt) {
		var parser = Jwts.parserBuilder().setSigningKey(getPublicKey()).build();

		// ? Will throw exceptions if fail
		parser.parseClaimsJws(jwt);

		return true;
	}

	public String getUsernameFromJwt(String jwt) {
		var parser = Jwts.parserBuilder().setSigningKey(getPublicKey()).build();
		var claims = parser.parseClaimsJws(jwt).getBody();

		return claims.getSubject();
	}

	private PrivateKey getPrivateKey() {
		try {
			return (PrivateKey) keyStore.getKey("springblog", "secret".toCharArray());
		} catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
			throw new UnknownException("Exception occured while retrieving private key from keystore");
		}
	}

	private PublicKey getPublicKey() {
		try {
			return keyStore.getCertificate("springblog").getPublicKey();
		} catch (KeyStoreException e) {
			throw new UnknownException("Exception occured while retrieving public key from keystore");
		}
	}
}
