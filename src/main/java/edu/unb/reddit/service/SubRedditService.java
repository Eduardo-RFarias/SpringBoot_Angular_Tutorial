package edu.unb.reddit.service;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.unb.reddit.dto.SubRedditDto;
import edu.unb.reddit.exception.NotFoundException;
import edu.unb.reddit.mapper.SubRedditMapper;
import edu.unb.reddit.repository.SubRedditRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SubRedditService {
	private final SubRedditRepository subRedditRepository;
	private final SubRedditMapper subRedditMapper;

	@Transactional
	public List<SubRedditDto> list() {
		return subRedditRepository.findAll().stream().map(subRedditMapper::mapSubRedditToDto).collect(toList());
	}

	@Transactional
	public SubRedditDto retrieve(Long id) {
		var subReddit = subRedditRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Could not find subreddit with Id " + id));

		return subRedditMapper.mapSubRedditToDto(subReddit);
	}

	@Transactional
	public SubRedditDto save(SubRedditDto subRedditDto) {
		var subReddit = subRedditRepository.save(subRedditMapper.mapDtoToSubreddit(subRedditDto));

		return subRedditMapper.mapSubRedditToDto(subReddit);
	}

	@Transactional
	public SubRedditDto update(Long id, SubRedditDto subRedditDto) {
		var subReddit = subRedditRepository.findById(id).orElseThrow(
				() -> new NotFoundException("Could not find subreddit with Id " + subRedditDto.getSubRedditId()));

		subReddit.setName(subRedditDto.getName());
		subReddit.setDescription(subRedditDto.getDescription());

		var savedSubReddit = subRedditRepository.save(subReddit);

		return subRedditMapper.mapSubRedditToDto(savedSubReddit);
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
