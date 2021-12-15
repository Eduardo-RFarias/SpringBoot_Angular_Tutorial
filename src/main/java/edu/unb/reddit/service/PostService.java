package edu.unb.reddit.service;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.unb.reddit.dto.PostRequest;
import edu.unb.reddit.dto.PostResponse;
import edu.unb.reddit.exception.NotFoundException;
import edu.unb.reddit.mapper.PostMapper;
import edu.unb.reddit.repository.PostRepository;
import edu.unb.reddit.repository.SubRedditRepository;
import edu.unb.reddit.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class PostService {
	private final SubRedditRepository subRedditRepository;
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final AuthService authService;
	private final PostMapper postMapper;

	public List<PostResponse> list() {
		return postRepository.findAll().stream().map(postMapper::entityToResponse).collect(toList());
	}

	public PostResponse create(PostRequest postRequest) {
		var subReddit = subRedditRepository.findByName(postRequest.getSubRedditName())
				.orElseThrow(() -> new NotFoundException("No subreddit found with given name"));

		var currentUser = authService.getCurrentUser();

		var newPost = postMapper.requestToEntity(postRequest, subReddit, currentUser);

		var savedPost = postRepository.save(newPost);

		return postMapper.entityToResponse(savedPost);
	}

	public PostResponse retrieveById(Long id) {
		var post = postRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("No post found with give Id: " + id));

		return postMapper.entityToResponse(post);
	}

	public List<PostResponse> retrieveBySubReddit(Long subRedditId) {
		var subReddit = subRedditRepository.findById(subRedditId)
				.orElseThrow(() -> new NotFoundException("No subreddit found with given Id: " + subRedditId));

		return postRepository.findAllBySubReddit(subReddit).stream().map(postMapper::entityToResponse)
				.collect(toList());
	}

	public List<PostResponse> retrieveByUsername(String username) {
		var user = userRepository.findByUsername(username)
				.orElseThrow(() -> new NotFoundException("No User found with the given username: " + username));

		return postRepository.findAllByUser(user).stream().map(postMapper::entityToResponse).collect(toList());
	}

	public PostResponse update(Long id, PostRequest postRequest) {
		var post = postRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("No post found with given Id: " + id));

		post.setPostName(postRequest.getPostName());
		post.setDescription(postRequest.getDescription());
		post.setUrl(postRequest.getUrl());

		var savedPost = postRepository.save(post);

		return postMapper.entityToResponse(savedPost);
	}

	public void delete(Long id) {
		try {
			postRepository.deleteById(id);
		} catch (EmptyResultDataAccessException error) {
			throw new NotFoundException("Could not find post with Id " + id);
		}
	}
}
