package com.tradedoubler.authenticationservice.services;

import com.tradedoubler.authenticationservice.model.User;
import com.tradedoubler.authenticationservice.model.AuthResponse;
import com.tradedoubler.authenticationservice.model.AuthenticationRequest;
import com.tradedoubler.authenticationservice.utils.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final AuthenticationManager authenticationManager;

  public AuthResponse authenticateUser(AuthenticationRequest authenticationRequest) {

    User user = (User) authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(),
                authenticationRequest.getPassword()))
        .getPrincipal();

    return AuthResponse.from(AuthenticationUtils.generateAuthToken(user),
        AuthenticationUtils.generateRefreshToken(user));
  }
}
