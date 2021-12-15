package edu.unb.reddit.service;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.unb.reddit.dto.CommentRequest;
import edu.unb.reddit.dto.CommentResponse;
import edu.unb.reddit.exception.NotFoundException;
import edu.unb.reddit.mapper.CommentMapper;
import edu.unb.reddit.repository.CommentRepository;
import edu.unb.reddit.repository.PostRepository;
import edu.unb.reddit.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class CommentService {
	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final AuthService authService;
	private final CommentMapper commentMapper;
	private final UserRepository userRepository;

	public List<CommentResponse> list() {
		return commentRepository.findAll().stream().map(commentMapper::entityToResponse).collect(toList());
	}

	public CommentResponse create(CommentRequest commentRequest) {
		var post = postRepository.findById(commentRequest.getPostId()).orElseThrow(
				() -> new NotFoundException("No comment found with given Id: " + commentRequest.getPostId()));

		var currentUser = authService.getCurrentUser();

		var saved = commentRepository.save(commentMapper.requestToEntity(commentRequest, post, currentUser));

		return commentMapper.entityToResponse(saved);
	}

	public CommentResponse retrieveById(Long id) {
		var comment = commentRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("No comment found with given Id: " + id));

		return commentMapper.entityToResponse(comment);
	}

	public List<CommentResponse> retrieveAllByUsername(String username) {
		var user = userRepository.findByUsername(username)
				.orElseThrow(() -> new NotFoundException("No user found with given username: " + username));

		return commentRepository.findAllByUser(user).stream().map(commentMapper::entityToResponse).collect(toList());
	}

	public List<CommentResponse> retrieveAllByPost(Long postId) {
		var post = postRepository.findById(postId)
				.orElseThrow(() -> new NotFoundException("No post found with given Id: " + postId));

		return commentRepository.findAllByPost(post).stream().map(commentMapper::entityToResponse).collect(toList());
	}

	public CommentResponse update(Long id, CommentRequest commentRequest) {
		var comment = commentRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("No comment found with given Id: " + id));

		comment.setText(commentRequest.getText());

		var saved = commentRepository.save(comment);

		return commentMapper.entityToResponse(saved);
	}

	public void delete(Long id) {
		try {
			commentRepository.deleteById(id);
		} catch (EmptyResultDataAccessException error) {
			throw new NotFoundException("Could not find comment with Id " + id);
		}
	}

}
