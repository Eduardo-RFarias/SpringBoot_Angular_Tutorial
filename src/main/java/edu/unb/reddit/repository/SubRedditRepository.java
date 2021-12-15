package edu.unb.reddit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.unb.reddit.model.SubReddit;

public interface SubRedditRepository extends JpaRepository<SubReddit, Long> {

}
