package edu.unb.reddit.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Service;

import edu.unb.reddit.controller.PostController;
import edu.unb.reddit.dto.PostResponse;

@Service
public class PostAssembler implements RepresentationModelAssembler<PostResponse, EntityModel<PostResponse>> {

	@Override
	public EntityModel<PostResponse> toModel(PostResponse entity) {
		var selfLink = linkTo(methodOn(PostController.class).retrieveById(entity.getPostId())).withSelfRel();
		var listLink = linkTo(methodOn(PostController.class).list()).withRel("posts");

		return EntityModel.of(entity, selfLink, listLink);
	}
}
