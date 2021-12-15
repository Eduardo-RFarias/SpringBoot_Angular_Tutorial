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

import edu.unb.reddit.assembler.PostAssembler;
import edu.unb.reddit.dto.PostRequest;
import edu.unb.reddit.dto.PostResponse;
import edu.unb.reddit.service.PostService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/post")
public class PostController {
	private final PostService postService;
	private final PostAssembler postAssembler;

	@GetMapping("/")
	public CollectionModel<EntityModel<PostResponse>> list() {
		var posts = postService.list().stream().map(postAssembler::toModel).collect(toList());

		var selfLink = linkTo(methodOn(getClass()).list()).withSelfRel();

		return CollectionModel.of(posts, selfLink);
	}

	@GetMapping("/{id}/")
	public EntityModel<PostResponse> retrieveById(@PathVariable Long id) {
		var post = postService.retrieveById(id);

		return postAssembler.toModel(post);
	}

	@GetMapping("/by-subreddit/{subRedditId}/")
	public CollectionModel<EntityModel<PostResponse>> retrieveBySubReddit(@PathVariable Long subRedditId) {
		var posts = postService.retrieveBySubReddit(subRedditId).stream().map(postAssembler::toModel).collect(toList());

		var selfLink = linkTo(methodOn(getClass()).retrieveBySubReddit(subRedditId)).withSelfRel();

		return CollectionModel.of(posts, selfLink);
	}

	@GetMapping("/by-user/{name}/")
	public CollectionModel<EntityModel<PostResponse>> retrieveByUsername(@PathVariable String name) {
		var posts = postService.retrieveByUsername(name).stream().map(postAssembler::toModel).collect(toList());

		var selfLink = linkTo(methodOn(getClass()).retrieveByUsername(name)).withSelfRel();

		return CollectionModel.of(posts, selfLink);
	}

	@PostMapping("/")
	public ResponseEntity<EntityModel<PostResponse>> create(@RequestBody PostRequest postRequest) {
		var post = postService.create(postRequest);
		var entity = postAssembler.toModel(post);

		var location = entity.getRequiredLink(IanaLinkRelations.SELF).toUri();

		return ResponseEntity.created(location).body(entity);
	}

	@PutMapping("/{id}/")
	public EntityModel<PostResponse> update(@PathVariable Long id, @RequestBody PostRequest postRequest) {
		var post = postService.update(id, postRequest);

		return postAssembler.toModel(post);
	}

	@DeleteMapping("/{id}/")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		postService.delete(id);

		return ResponseEntity.noContent().build();
	}
}
