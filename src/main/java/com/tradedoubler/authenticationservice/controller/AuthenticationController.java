package com.tradedoubler.authenticationservice.controller;

import com.tradedoubler.authenticationservice.model.AuthenticationRequest;
import com.tradedoubler.authenticationservice.model.RegistrationRequest;
import com.tradedoubler.authenticationservice.services.AuthenticationService;
import com.tradedoubler.authenticationservice.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final UserService userService;

  private final AuthenticationService authenticationService;

  @PostMapping(value = "login")
  public ResponseEntity<?> loginUser(
      @RequestBody @Valid AuthenticationRequest authenticationRequest) {
    return ResponseEntity.ok(authenticationService.authenticateUser(authenticationRequest));
  }

  @PostMapping(value = "register")
  public ResponseEntity<?> registerUser(
      @RequestBody @Valid RegistrationRequest registrationRequest) {
      return ResponseEntity.ok(userService.registerUser(registrationRequest));
  }
}