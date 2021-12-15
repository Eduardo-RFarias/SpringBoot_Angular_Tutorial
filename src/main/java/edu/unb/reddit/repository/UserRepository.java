package edu.unb.reddit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.unb.reddit.model.RedditUser;

public interface UserRepository extends JpaRepository<RedditUser, Long> {

	Optional<RedditUser> findByUsername(String username);

}
