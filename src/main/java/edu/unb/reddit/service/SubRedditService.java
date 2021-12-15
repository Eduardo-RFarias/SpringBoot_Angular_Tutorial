package edu.unb.reddit.service;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.unb.reddit.dto.SubRedditRequest;
import edu.unb.reddit.dto.SubRedditResponse;
import edu.unb.reddit.exception.NotFoundException;
import edu.unb.reddit.mapper.SubRedditMapper;
import edu.unb.reddit.repository.SubRedditRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class SubRedditService {
	private final SubRedditRepository subRedditRepository;
	private final SubRedditMapper subRedditMapper;
	private final AuthService authService;

	public List<SubRedditResponse> list() {
		return subRedditRepository.findAll().stream().map(subRedditMapper::entityToResponse).collect(toList());
	}

	public SubRedditResponse retrieve(Long id) {
		var subReddit = subRedditRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Could not find subreddit with Id " + id));

		return subRedditMapper.entityToResponse(subReddit);
	}

	public SubRedditResponse save(SubRedditRequest subRedditDto) {
		var currentUser = authService.getCurrentUser();
		var subReddit = subRedditRepository.save(subRedditMapper.requestToEntity(subRedditDto, currentUser));

		return subRedditMapper.entityToResponse(subReddit);
	}

	public SubRedditResponse update(Long id, SubRedditRequest subRedditDto) {
		var subReddit = subRedditRepository.findById(id).orElseThrow(
				() -> new NotFoundException("Could not find subreddit with Id " + subRedditDto.getSubRedditId()));

		subReddit.setName(subRedditDto.getName());
		subReddit.setDescription(subRedditDto.getDescription());

		var savedSubReddit = subRedditRepository.save(subReddit);

		return subRedditMapper.entityToResponse(savedSubReddit);
	}

	public void delete(Long id) {
		try {
			subRedditRepository.deleteById(id);
		} catch (EmptyResultDataAccessException error) {
			throw new NotFoundException("Could not find subreddit with Id " + id);
		}
	}

}
