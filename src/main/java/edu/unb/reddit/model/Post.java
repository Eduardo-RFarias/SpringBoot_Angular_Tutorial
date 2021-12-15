package edu.unb.reddit.model;

import static javax.persistence.FetchType.LAZY;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
	@Id
	@GeneratedValue
	private Long postId;

	@NotBlank(message = "Post Name cannot be empty or Null")
	private String postName;

	@Nullable
	private String url;

	@Nullable
	@Lob
	private String description;

	private Integer voteCount;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "redditUserId", referencedColumnName = "redditUserId")
	private RedditUser user;

	private Instant createdDate;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "subRedditId", referencedColumnName = "subRedditId")
	private SubReddit subReddit;

}
