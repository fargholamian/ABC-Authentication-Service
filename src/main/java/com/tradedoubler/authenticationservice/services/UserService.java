package com.tradedoubler.authenticationservice.services;

import static com.tradedoubler.authenticationservice.enums.Role.ROLE_USER;
import static com.tradedoubler.authenticationservice.utils.AuthenticationUtils.encodePassword;
import static com.tradedoubler.authenticationservice.utils.AuthenticationUtils.generateAuthToken;
import static com.tradedoubler.authenticationservice.utils.AuthenticationUtils.generateRefreshToken;

import com.tradedoubler.authenticationservice.model.User;
import com.tradedoubler.authenticationservice.model.AuthResponse;
import com.tradedoubler.authenticationservice.model.RegistrationRequest;
import com.tradedoubler.authenticationservice.repo.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;

  public AuthResponse registerUser(RegistrationRequest registrationRequest) {

    String encodedPassword = encodePassword(registrationRequest.getPassword());

    try {
      User user = userRepository.save(User
          .builder()
          .id(UUID.randomUUID())
          .username(registrationRequest.getUsername())
          .password(encodedPassword)
          .role(ROLE_USER)
          .build());
      return AuthResponse.from(generateAuthToken(user), generateRefreshToken(user));
    } catch (DataIntegrityViolationException e) {
      throw new RuntimeException("User already exists");
    } catch (Exception e) {
      throw new RuntimeException("Something went wrong when saving the user in the database");
    }
  }

  @Override
  public User loadUserByUsername(String username) {
    return userRepository
        .findByUsername(username)
        .orElseThrow(() ->
            new UsernameNotFoundException("No user found for the provided username."));
  }
}
