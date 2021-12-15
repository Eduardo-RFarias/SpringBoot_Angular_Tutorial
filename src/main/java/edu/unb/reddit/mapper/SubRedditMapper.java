package edu.unb.reddit.mapper;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import edu.unb.reddit.dto.SubRedditRequest;
import edu.unb.reddit.dto.SubRedditResponse;
import edu.unb.reddit.model.Post;
import edu.unb.reddit.model.RedditUser;
import edu.unb.reddit.model.SubReddit;

@Mapper(componentModel = "spring")
public interface SubRedditMapper {

	@Mapping(target = "user", source = "redditUser")
	@Mapping(target = "createdDate", expression = "java(defaultCreatedDate())")
	@Mapping(target = "posts", expression = "java(emptyPostList())")
	SubReddit requestToEntity(SubRedditRequest subRedditRequest, RedditUser redditUser);

	@Mapping(target = "user", source = "redditUser")
	@Mapping(target = "createdDate", expression = "java(defaultCreatedDate())")
	@Mapping(target = "posts", expression = "java(emptyPostList())")
	SubReddit responseToEntity(SubRedditResponse subRedditRequest, RedditUser redditUser);

	@Mapping(target = "username", source = "user.username")
	SubRedditRequest entityToRequest(SubReddit subReddit);

	@Mapping(target = "numberOfPosts", expression = "java(mapPosts(subReddit))")
	@Mapping(target = "username", source = "user.username")
	SubRedditResponse entityToResponse(SubReddit subReddit);

	default Integer mapPosts(SubReddit subReddit) {
		List<Post> posts = subReddit.getPosts();
		return posts != null ? posts.size() : 0;
	}

	default List<Post> emptyPostList() {
		return new ArrayList<>();
	}

	default Instant defaultCreatedDate() {
		return Instant.now();
	}
}
