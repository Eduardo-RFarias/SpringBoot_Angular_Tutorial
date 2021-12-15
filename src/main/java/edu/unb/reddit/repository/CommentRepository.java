package edu.unb.reddit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.unb.reddit.model.Comment;
import edu.unb.reddit.model.Post;
import edu.unb.reddit.model.RedditUser;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	List<Comment> findAllByUser(RedditUser user);

	List<Comment> findAllByPost(Post post);

}
