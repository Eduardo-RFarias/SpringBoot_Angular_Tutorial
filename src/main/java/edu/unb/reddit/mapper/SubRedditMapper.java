package edu.unb.reddit.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import edu.unb.reddit.dto.SubRedditDto;
import edu.unb.reddit.model.Post;
import edu.unb.reddit.model.SubReddit;

@Mapper(componentModel = "spring")
public interface SubRedditMapper {

	@Mapping(target = "numberOfPosts", expression = "java(mapPosts(subReddit.getPosts()))")
	SubRedditDto mapSubRedditToDto(SubReddit subReddit);

	@InheritInverseConfiguration
	@Mapping(target = "createdDate", ignore = true)
	@Mapping(target = "user", ignore = true)
	@Mapping(target = "posts", ignore = true)
	SubReddit mapDtoToSubreddit(SubRedditDto subRedditDto);

	default Integer mapPosts(List<Post> posts) {
		return posts != null ? posts.size() : 0;
	}
}
