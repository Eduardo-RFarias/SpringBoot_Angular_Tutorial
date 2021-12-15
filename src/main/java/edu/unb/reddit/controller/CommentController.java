package edu.unb.reddit.controller;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.unb.reddit.assembler.CommentAssembler;
import edu.unb.reddit.dto.CommentRequest;
import edu.unb.reddit.dto.CommentResponse;
import edu.unb.reddit.service.CommentService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {
	private final CommentService commentService;
	private final CommentAssembler commentAssembler;

	@GetMapping("/")
	public CollectionModel<EntityModel<CommentResponse>> list() {
		var comments = commentService.list().stream().map(commentAssembler::toModel).collect(toList());

		var selfLink = linkTo(methodOn(getClass()).list()).withSelfRel();

		return CollectionModel.of(comments, selfLink);
	}

	@PostMapping("/")
	public ResponseEntity<EntityModel<CommentResponse>> create(@RequestBody CommentRequest commentRequest) {
		var comment = commentService.create(commentRequest);
		var entity = commentAssembler.toModel(comment);

		var location = entity.getRequiredLink(IanaLinkRelations.SELF).toUri();

		return ResponseEntity.created(location).body(entity);
	}

	@GetMapping("/{id}/")
	public EntityModel<CommentResponse> retrieveById(@PathVariable Long id) {
		var comment = commentService.retrieveById(id);

		return commentAssembler.toModel(comment);
	}

	@GetMapping("/by-postId/{postId}/")
	public CollectionModel<EntityModel<CommentResponse>> retrieveAllByPost(@PathVariable Long postId) {
		var comments = commentService.retrieveAllByPost(postId).stream().map(commentAssembler::toModel)
				.collect(toList());

		var selfLink = linkTo(methodOn(getClass()).retrieveAllByPost(postId)).withSelfRel();

		return CollectionModel.of(comments, selfLink);
	}

	@GetMapping("/by-user/{username}/")
	public CollectionModel<EntityModel<CommentResponse>> retrieveAllByUsername(@PathVariable String username) {
		var comments = commentService.retrieveAllByUsername(username).stream().map(commentAssembler::toModel)
				.collect(toList());

		var selfLink = linkTo(methodOn(getClass()).retrieveAllByUsername(username)).withSelfRel();

		return CollectionModel.of(comments, selfLink);
	}

	@PutMapping("/{id}/")
	public EntityModel<CommentResponse> update(@PathVariable Long id, @RequestBody CommentRequest commentRequest) {
		var comment = commentService.update(id, commentRequest);

		return commentAssembler.toModel(comment);
	}

	@DeleteMapping("/{id}/")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		commentService.delete(id);

		return ResponseEntity.noContent().build();
	}
}
