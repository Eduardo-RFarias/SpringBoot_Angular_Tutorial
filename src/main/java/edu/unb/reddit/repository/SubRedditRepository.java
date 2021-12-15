package edu.unb.reddit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.unb.reddit.model.SubReddit;

public interface SubRedditRepository extends JpaRepository<SubReddit, Long> {

	Optional<SubReddit> findByName(String name);
}
