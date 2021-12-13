package com.example.reddit.service;

import static java.util.Collections.singletonList;

import java.util.List;

import com.example.reddit.model.RedditUser;
import com.example.reddit.repository.UserRepository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        RedditUser user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User not found with the given username: " + username));

        return new User(
                user.getUsername(),
                user.getPassword(),
                user.getEnabled(),
                true,
                true,
                true,
                getAuthorities("USER"));
    }

    private List<SimpleGrantedAuthority> getAuthorities(String role) {
        return singletonList(new SimpleGrantedAuthority(role));
    }
}
