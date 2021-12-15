package edu.unb.reddit.mapper;

import java.time.Instant;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import edu.unb.reddit.dto.CommentRequest;
import edu.unb.reddit.dto.CommentResponse;
import edu.unb.reddit.model.Comment;
import edu.unb.reddit.model.Post;
import edu.unb.reddit.model.RedditUser;

@Mapper(componentModel = "spring")
public interface CommentMapper {

	@Mapping(target = "post", source = "post")
	@Mapping(target = "user", source = "redditUser")
	@Mapping(target = "createdDate", expression = "java(defaultCreatedDate())")
	Comment requestToEntity(CommentRequest commentRequest, Post post, RedditUser redditUser);

	@Mapping(target = "post", source = "post")
	@Mapping(target = "user", source = "redditUser")
	@Mapping(target = "createdDate", expression = "java(defaultCreatedDate())")
	Comment responseToEntity(CommentResponse commentResponse, Post post, RedditUser redditUser);

	@Mapping(target = "postId", source = "post.postId")
	@Mapping(target = "username", source = "user.username")
	CommentRequest entityToRequest(Comment comment);

	@Mapping(target = "postName", source = "post.postName")
	@Mapping(target = "username", source = "user.username")
	CommentResponse entityToResponse(Comment comment);

	default Instant defaultCreatedDate() {
		return Instant.now();
	}
}
