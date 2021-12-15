package edu.unb.reddit.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Service;

import edu.unb.reddit.controller.SubRedditController;
import edu.unb.reddit.dto.SubRedditDto;

@Service
public class SubRedditAssembler implements RepresentationModelAssembler<SubRedditDto, EntityModel<SubRedditDto>> {

	@Override
	public EntityModel<SubRedditDto> toModel(SubRedditDto entity) {
		var selfLink = linkTo(methodOn(SubRedditController.class).retrieve(entity.getSubRedditId())).withSelfRel();
		var listLink = linkTo(methodOn(SubRedditController.class).list()).withRel("subreddits");

		return EntityModel.of(entity, selfLink, listLink);
	}

}
