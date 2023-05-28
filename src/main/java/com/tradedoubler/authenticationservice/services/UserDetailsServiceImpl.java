package com.tradedoubler.authenticationservice.services;

import com.tradedoubler.authenticationservice.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) {
    return userRepository
        .findByUsername(username)
        .orElseThrow(() ->
            new UsernameNotFoundException("No user found for the provided username."));
  }
}