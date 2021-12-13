package com.example.reddit.repository;

import java.util.Optional;

import com.example.reddit.model.RedditUser;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<RedditUser, Long> {

    Optional<RedditUser> findByUsername(String username);

}
