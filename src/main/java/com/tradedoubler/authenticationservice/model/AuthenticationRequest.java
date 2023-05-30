package com.tradedoubler.authenticationservice.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

  @NotBlank(message = "username must be provided.")
  private String username;

  @NotBlank(message = "password must be provided.")
  private String password;
}
