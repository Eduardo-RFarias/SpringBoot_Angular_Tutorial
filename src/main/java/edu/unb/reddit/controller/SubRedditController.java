package edu.unb.reddit.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.LinkedList;
import java.util.List;

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

import edu.unb.reddit.assembler.SubRedditAssembler;
import edu.unb.reddit.dto.SubRedditDto;
import edu.unb.reddit.service.SubRedditService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor
public class SubRedditController {
	private final SubRedditService subRedditService;
	private final SubRedditAssembler subRedditAssembler;

	@GetMapping("/")
	public CollectionModel<EntityModel<SubRedditDto>> list() {
		var subReddits = subRedditService.list();

		List<EntityModel<SubRedditDto>> subRedditsWithLinks = new LinkedList<>();

		for (var subReddit : subReddits) {
			subRedditsWithLinks.add(subRedditAssembler.toModel(subReddit));
		}

		var selfLink = linkTo(methodOn(getClass()).list()).withSelfRel();

		return CollectionModel.of(subRedditsWithLinks, selfLink);
	}

	@GetMapping("/{id}")
	public EntityModel<SubRedditDto> retrieve(@PathVariable Long id) {
		var subReddit = subRedditService.retrieve(id);

		return subRedditAssembler.toModel(subReddit);
	}

	@PostMapping("/")
	public ResponseEntity<EntityModel<SubRedditDto>> create(@RequestBody SubRedditDto subRedditDto) {
		var savedSubReddit = subRedditService.save(subRedditDto);
		var entity = subRedditAssembler.toModel(savedSubReddit);

		var location = entity.getRequiredLink(IanaLinkRelations.SELF).toUri();

		return ResponseEntity.created(location).body(entity);
	}

	@PutMapping("/{id}")
	public EntityModel<SubRedditDto> update(@PathVariable Long id, @RequestBody SubRedditDto subRedditDto) {
		var subReddit = subRedditService.update(id, subRedditDto);

		return subRedditAssembler.toModel(subReddit);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		subRedditService.delete(id);

		return ResponseEntity.noContent().build();
	}
}
