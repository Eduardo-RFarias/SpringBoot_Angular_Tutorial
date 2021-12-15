package edu.unb.reddit.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Service;

import edu.unb.reddit.controller.CommentController;
import edu.unb.reddit.dto.CommentResponse;

@Service
public class CommentAssembler implements RepresentationModelAssembler<CommentResponse, EntityModel<CommentResponse>> {

	@Override
	public EntityModel<CommentResponse> toModel(CommentResponse entity) {
		var selfLink = linkTo(methodOn(CommentController.class).retrieveById(entity.getCommentId())).withSelfRel();
		var listLink = linkTo(methodOn(CommentController.class).list()).withRel("posts");

		return EntityModel.of(entity, selfLink, listLink);
	}

}
