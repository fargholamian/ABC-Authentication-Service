package com.tradedoubler.authenticationservice.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {

  @NotBlank(message = "username must be provided.")
  private String username;

  @NotBlank(message = "password must be provided.")
  private String password;
}
