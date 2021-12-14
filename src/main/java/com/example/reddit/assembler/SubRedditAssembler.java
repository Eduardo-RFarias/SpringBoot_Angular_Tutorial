package com.example.reddit.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.example.reddit.controller.SubRedditController;
import com.example.reddit.model.SubReddit;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Service;

@Service
public class SubRedditAssembler implements RepresentationModelAssembler<SubReddit, EntityModel<SubReddit>> {

    @Override
    public EntityModel<SubReddit> toModel(SubReddit entity) {
        Link selfLink = linkTo(methodOn(SubRedditController.class).retrieve(entity.getSubRedditId())).withSelfRel();
        Link listLink = linkTo(methodOn(SubRedditController.class).list()).withRel("subreddits");

        return EntityModel.of(entity, selfLink, listLink);
    }

}
