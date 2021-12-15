package edu.unb.reddit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.unb.reddit.model.Post;
import edu.unb.reddit.model.RedditUser;
import edu.unb.reddit.model.SubReddit;

public interface PostRepository extends JpaRepository<Post, Long> {
	List<Post> findAllBySubReddit(SubReddit subReddit);

	List<Post> findAllByUser(RedditUser redditUser);
}
