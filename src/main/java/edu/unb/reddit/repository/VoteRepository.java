package edu.unb.reddit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.unb.reddit.model.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long> {

}
