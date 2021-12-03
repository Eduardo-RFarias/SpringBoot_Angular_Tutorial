package com.example.reddit.repository;

import com.example.reddit.model.Vote;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {

}
