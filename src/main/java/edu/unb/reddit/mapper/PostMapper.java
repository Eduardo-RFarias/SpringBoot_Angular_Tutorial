package edu.unb.reddit.mapper;

import java.time.Instant;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import edu.unb.reddit.dto.PostRequest;
import edu.unb.reddit.dto.PostResponse;
import edu.unb.reddit.model.Post;
import edu.unb.reddit.model.RedditUser;
import edu.unb.reddit.model.SubReddit;

@Mapper(componentModel = "spring")
public interface PostMapper {

	@Mapping(target = "subReddit", source = "subReddit")
	@Mapping(target = "user", source = "redditUser")
	@Mapping(target = "createdDate", expression = "java(defaultCreatedDate())")
	@Mapping(target = "description", source = "postRequest.description")
	@Mapping(target = "voteCount", expression = "java(defaultVoteCount())")
	Post requestToEntity(PostRequest postRequest, SubReddit subReddit, RedditUser redditUser);

	@Mapping(target = "subReddit", source = "subReddit")
	@Mapping(target = "user", source = "redditUser")
	@Mapping(target = "description", source = "postResponse.description")
	@Mapping(target = "createdDate", expression = "java(defaultCreatedDate())")
	Post responseToEntity(PostResponse postResponse, SubReddit subReddit, RedditUser redditUser);

	@Mapping(target = "subRedditName", source = "subReddit.name")
	@Mapping(target = "username", source = "user.username")
	PostRequest entityToRequest(Post post);

	@Mapping(target = "subredditName", source = "subReddit.name")
	@Mapping(target = "username", source = "user.username")
	PostResponse entityToResponse(Post post);

	default Integer defaultVoteCount() {
		return Integer.valueOf(0);
	}

	default Instant defaultCreatedDate() {
		return Instant.now();
	}
}
