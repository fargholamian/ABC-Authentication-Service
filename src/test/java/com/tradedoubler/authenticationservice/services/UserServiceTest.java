package com.tradedoubler.authenticationservice.services;

import static com.tradedoubler.authenticationservice.utils.AuthenticationUtils.getSubjectFromToken;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tradedoubler.authenticationservice.BaseServiceTest;
import com.tradedoubler.authenticationservice.model.User;
import com.tradedoubler.authenticationservice.model.AuthResponse;
import com.tradedoubler.authenticationservice.model.RegistrationRequest;
import com.tradedoubler.authenticationservice.repo.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Import({ UserService.class })
public class UserServiceTest extends BaseServiceTest {

  @Autowired
  private UserService userService;

  @MockBean
  private UserRepository userRepository;

  private final RegistrationRequest registrationRequest = RegistrationRequest.builder()
      .username("test-username")
        .password("test-password")
        .confirmPassword("test-password")
        .build();
  @Test
  public void shouldRegisterUserReturnsNewUser_WhenRepositoryReturnsOk() {
    when(userRepository.save(any(User.class)))
        .thenReturn(User.builder().username("test-username").build());

    AuthResponse authResponse = userService.registerUser(registrationRequest);

    assertThat(authResponse).isNotNull();
    assertThat(authResponse.getType()).isEqualTo("Bearer");
    assertThat(getSubjectFromToken(authResponse.getAuthToken()))
        .isEqualTo("test-username");
    assertThat(getSubjectFromToken(authResponse.getRefreshToken()))
        .isEqualTo("test-username");
    verify(userRepository, times(1))
        .save(any(User.class));
  }

  @Test
  public void shouldRegisterUserThrowException_WhenRepositoryThrowException() {

    when(userRepository.save(any(User.class)))
        .thenThrow(new IllegalArgumentException("Something is wrong"));

    Exception exception = assertThrows(RuntimeException.class, () -> userService.registerUser(registrationRequest));

    assertTrue(exception.getMessage().contains("Something went wrong when saving the user in the database"));
  }

  @Test
  public void shouldRegisterUserThrowException_WhenTheUserAlreadyExists() {

    when(userRepository.save(any(User.class)))
        .thenThrow(new DataIntegrityViolationException("user exists"));

    Exception exception = assertThrows(RuntimeException.class, () -> userService.registerUser(registrationRequest));

    assertTrue(exception.getMessage().contains("User already exists"));
  }

  @Test
  public void shouldLoadUserByUsernameReturnsTheUser_WhenRepositoryReturnsOk() {
    User expectedUsr = User.builder().username("test-username").build();
    when(userRepository.findByUsername("test-username"))
        .thenReturn(Optional.of(expectedUsr));

    User user = userService.loadUserByUsername("test-username");

    assertThat(user.getUsername()).isEqualTo(expectedUsr.getUsername());
    verify(userRepository, times(1))
        .findByUsername(any(String.class));
  }

  @Test
  public void shouldLoadUserByUsernameThrowException_WhenRepositoryReturnsOptionalEmpty() {
    when(userRepository.findByUsername("test-username"))
        .thenReturn(Optional.empty());

    Exception exception = assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("test-username"));
    assertTrue(exception.getMessage().contains("No user found for the provided username."));
  }
}