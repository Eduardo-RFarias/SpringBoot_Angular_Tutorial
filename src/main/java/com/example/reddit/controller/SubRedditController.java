package com.example.reddit.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import com.example.reddit.assembler.SubRedditAssembler;
import com.example.reddit.dto.SubRedditDto;
import com.example.reddit.model.SubReddit;
import com.example.reddit.service.SubRedditService;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor
public class SubRedditController {
    private final SubRedditService subRedditService;
    private final SubRedditAssembler subRedditAssembler;

    @GetMapping("/")
    public CollectionModel<EntityModel<SubReddit>> list() {
        List<SubReddit> subReddits = subRedditService.list();

        List<EntityModel<SubReddit>> subRedditsWithLinks = new LinkedList<>();

        for (SubReddit subReddit : subReddits) {
            subRedditsWithLinks.add(subRedditAssembler.toModel(subReddit));
        }

        Link selfLink = linkTo(methodOn(getClass()).list()).withSelfRel();

        return CollectionModel.of(subRedditsWithLinks, selfLink);
    }

    @GetMapping("/{id}")
    public EntityModel<SubReddit> retrieve(@PathVariable Long id) {
        SubReddit subReddit = subRedditService.retrieve(id);

        return subRedditAssembler.toModel(subReddit);
    }

    @PostMapping("/")
    public ResponseEntity<EntityModel<SubReddit>> create(@RequestBody SubRedditDto subRedditDto) {
        SubReddit savedSubReddit = subRedditService.save(subRedditDto);
        EntityModel<SubReddit> entity = subRedditAssembler.toModel(savedSubReddit);

        URI location = entity.getRequiredLink(IanaLinkRelations.SELF).toUri();

        return ResponseEntity.created(location).body(entity);
    }

    @PutMapping("/{id}")
    public EntityModel<SubReddit> update(@PathVariable Long id, @RequestBody SubRedditDto subRedditDto) {
        SubReddit subReddit = subRedditService.update(id, subRedditDto);

        return subRedditAssembler.toModel(subReddit);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        subRedditService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
