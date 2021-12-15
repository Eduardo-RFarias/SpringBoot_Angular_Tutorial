package edu.unb.reddit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.unb.reddit.model.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

	Optional<VerificationToken> findByToken(String token);

}
