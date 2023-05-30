package com.tradedoubler.authenticationservice.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
@Valid
public class RegistrationRequest extends AuthenticationRequest {

  @NotBlank(message = "confirm_password must provided.")
  @JsonProperty("confirm_password")
  private String confirmPassword;
}