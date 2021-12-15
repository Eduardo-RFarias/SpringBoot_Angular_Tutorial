package edu.unb.reddit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubRedditResponse {
	private Long subRedditId;
	private String name;
	private String description;
	private String username;
	private Integer numberOfPosts;
}