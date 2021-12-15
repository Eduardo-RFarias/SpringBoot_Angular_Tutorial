package edu.unb.reddit.model;

import static javax.persistence.FetchType.LAZY;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Comment {
	@Id
	@GeneratedValue
	private Long commentId;

	@NotEmpty
	private String text;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "postId", referencedColumnName = "postId")
	private Post post;

	private Instant createdDate;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "redditUserId", referencedColumnName = "redditUserId")
	private RedditUser user;
}
