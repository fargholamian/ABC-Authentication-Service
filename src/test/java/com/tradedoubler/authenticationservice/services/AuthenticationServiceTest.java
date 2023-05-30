package com.tradedoubler.authenticationservice.services;

import static com.tradedoubler.authenticationservice.utils.AuthenticationUtils.getSubjectFromToken;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import com.tradedoubler.authenticationservice.BaseServiceTest;
import com.tradedoubler.authenticationservice.model.User;
import com.tradedoubler.authenticationservice.model.AuthResponse;
import com.tradedoubler.authenticationservice.model.AuthenticationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Import({ AuthenticationService.class })
public class AuthenticationServiceTest extends BaseServiceTest {

  @Autowired
  private AuthenticationService authenticationService;

  @MockBean
  private AuthenticationManager authenticationManager;

  @Test
  public void shouldAuthenticateUserReturnCorrectAuthResponse_WhenAuthenticationRequestIsValid() {

    AuthenticationRequest authenticationRequest = new AuthenticationRequest("test-username", "test-password");
    UsernamePasswordAuthenticationToken authentication =
        mock(UsernamePasswordAuthenticationToken.class);

    when(authentication.getPrincipal())
        .thenReturn(User.builder().username("test-username").build());
    when(authenticationManager.authenticate(
        any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authentication);

    AuthResponse authResponse = authenticationService
        .authenticateUser(authenticationRequest);

    assertThat(authResponse).isNotNull();
    assertThat(authResponse.getType()).isEqualTo("Bearer");
    assertThat(getSubjectFromToken(authResponse.getAuthToken()))
        .isEqualTo("test-username");
    assertThat(getSubjectFromToken(authResponse.getRefreshToken()))
        .isEqualTo("test-username");
    verify(authenticationManager, times(1))
        .authenticate(any(UsernamePasswordAuthenticationToken.class));
  }
}