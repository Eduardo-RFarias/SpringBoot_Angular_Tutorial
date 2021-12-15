package edu.unb.reddit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
	private Long commentId;
	private String Text;
	private String username;
	private Long postId;
}
