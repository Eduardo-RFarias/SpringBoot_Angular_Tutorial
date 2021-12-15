package edu.unb.reddit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.unb.reddit.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

}
