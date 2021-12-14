package com.example.reddit.service;

import java.util.List;

import com.example.reddit.dto.SubRedditDto;
import com.example.reddit.exception.NotFoundException;
import com.example.reddit.model.SubReddit;
import com.example.reddit.repository.SubRedditRepository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SubRedditService {
    private final SubRedditRepository subRedditRepository;

    @Transactional
    public List<SubReddit> list() {
        return subRedditRepository.findAll();
    }

    @Transactional
    public SubReddit retrieve(Long id) {
        return subRedditRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Could not find subreddit with Id " + id));
    }

    @Transactional
    public SubReddit save(SubRedditDto subRedditDto) {
        SubReddit subReddit = SubReddit.builder()
                .name(subRedditDto.getName())
                .description(subRedditDto.getDescription())
                .build();

        return subRedditRepository.save(subReddit);
    }

    @Transactional
    public SubReddit update(Long id, SubRedditDto subRedditDto) {
        SubReddit subReddit = subRedditRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException("Could not find subreddit with Id " + subRedditDto.getId()));

        subReddit.setName(subRedditDto.getName());
        subReddit.setDescription(subRedditDto.getDescription());

        return subRedditRepository.save(subReddit);
    }

    @Transactional
    public void delete(Long id) {
        try {
            subRedditRepository.deleteById(id);
        } catch (EmptyResultDataAccessException error) {
            throw new NotFoundException("Could not find subreddit with Id " + id);
        }
    }

}
