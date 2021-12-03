package com.example.reddit.repository;

import com.example.reddit.model.SubReddit;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubRedditRepository extends JpaRepository<SubReddit, Long> {

}
