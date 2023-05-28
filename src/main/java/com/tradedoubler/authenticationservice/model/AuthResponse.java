package com.tradedoubler.authenticationservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.tradedoubler.authenticationservice.utils.AuthenticationUtils.SecurityConstants.HEADER_REQUEST_TOKEN_VALUE_PREFIX;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

  private String type;

  @JsonProperty(value = "auth_token")
  private String authToken;

  @JsonProperty(value = "refresh_token")
  private String refreshToken;

  public static AuthResponse from(String authToken, String refreshToken) {
    return AuthResponse
        .builder()
        .type(HEADER_REQUEST_TOKEN_VALUE_PREFIX.trim())
        .authToken(authToken)
        .refreshToken(refreshToken)
        .build();
  }
}