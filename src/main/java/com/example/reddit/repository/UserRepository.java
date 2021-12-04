package com.example.reddit.repository;

import java.util.Optional;

import com.example.reddit.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

}
