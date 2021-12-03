package com.example.reddit.repositories;

import com.example.reddit.models.SubReddit;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubRedditRepository extends JpaRepository<SubReddit, Long> {

}
