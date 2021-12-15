package edu.unb.reddit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.unb.reddit.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
