package com.tradedoubler.authenticationservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradedoubler.authenticationservice.model.AuthResponse;
import com.tradedoubler.authenticationservice.model.AuthenticationRequest;
import com.tradedoubler.authenticationservice.model.RegistrationRequest;
import com.tradedoubler.authenticationservice.services.AuthenticationService;
import com.tradedoubler.authenticationservice.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthenticationControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @MockBean
  private AuthenticationService authenticationService;

  @Test
  public void shouldRegisterUserAPIReturn200_WhenRegisteringNonExistingUser() throws Exception {
    AuthResponse expectedAuthResp = new AuthResponse("Bearer", "auth", "refresh");

    when(userService.registerUser(any())).thenReturn(expectedAuthResp);
    ResultActions resultActions = mockMvc.perform(
            post("/api/auth/register")
                .content(new ObjectMapper().writeValueAsString(buildRegistrationRequest()))
                .contentType(APPLICATION_JSON_VALUE));

    resultActions.andExpect(status().isOk());
    resultActions.andExpect(jsonPath("$.type").value(expectedAuthResp.getType()));
    resultActions.andExpect(jsonPath("$.auth_token").value(expectedAuthResp.getAuthToken()));
    resultActions.andExpect(jsonPath("$.refresh_token").value(expectedAuthResp.getRefreshToken()));
  }

  @Test
  public void shouldRegisterUserAPIReturn400_WhenRegisterRequestIsWrong() throws Exception {
    ResultActions resultActions = mockMvc.perform(
        post("/api/auth/register")
            .content(new ObjectMapper().writeValueAsString(buildAuthenticationRequest()))
            .contentType(APPLICATION_JSON_VALUE));

    resultActions.andExpect(status().isBadRequest());
    resultActions.andExpect(jsonPath("$.status").value("BAD_REQUEST"));
    resultActions.andExpect(jsonPath("$.message").value("Bad Request"));
  }

  @Test
  public void shouldRegisterUserAPIReturn400_WhenUserAlreadyExists() throws Exception {
    when(userService.registerUser(any())).thenThrow(new RuntimeException("User already exists"));
    ResultActions resultActions = mockMvc.perform(
        post("/api/auth/register")
            .content(new ObjectMapper().writeValueAsString(buildRegistrationRequest()))
            .contentType(APPLICATION_JSON_VALUE));

    resultActions.andExpect(status().isInternalServerError());
    resultActions.andExpect(jsonPath("$.status").value("INTERNAL_SERVER_ERROR"));
    resultActions.andExpect(jsonPath("$.message").value("User already exists"));
  }

  @Test
  public void shouldLoginAPIReturn200_WhenCredentialIsCorrect() throws Exception {
    AuthResponse expectedAuthResp = new AuthResponse("Bearer", "auth", "refresh");

    when(authenticationService.authenticateUser(any())).thenReturn(expectedAuthResp);

    ResultActions resultActions = mockMvc.perform(
        post("/api/auth/login")
            .content(new ObjectMapper().writeValueAsString(buildAuthenticationRequest()))
            .contentType(APPLICATION_JSON_VALUE));

    resultActions.andExpect(status().isOk());
    resultActions.andExpect(jsonPath("$.type").value(expectedAuthResp.getType()));
    resultActions.andExpect(jsonPath("$.auth_token").value(expectedAuthResp.getAuthToken()));
    resultActions.andExpect(jsonPath("$.refresh_token").value(expectedAuthResp.getRefreshToken()));
  }

  @Test
  public void shouldLoginUserAPIReturn400_WhenLoginRequestIsWrong() throws Exception {
    ResultActions resultActions = mockMvc.perform(
        post("/api/auth/register")
            .contentType(APPLICATION_JSON_VALUE));

    resultActions.andExpect(status().isBadRequest());
    resultActions.andExpect(jsonPath("$.status").value("BAD_REQUEST"));
    resultActions.andExpect(jsonPath("$.message").value("Bad Request"));
  }

  @Test
  public void shouldRegisterUserAPIReturn400_WhenUserCredentialIsNotCorrect() throws Exception {
    when(userService.registerUser(any())).thenThrow(new BadCredentialsException("Bad credentials"));
    ResultActions resultActions = mockMvc.perform(
        post("/api/auth/register")
            .content(new ObjectMapper().writeValueAsString(buildRegistrationRequest()))
            .contentType(APPLICATION_JSON_VALUE));

    resultActions.andExpect(status().isInternalServerError());
    resultActions.andExpect(jsonPath("$.status").value("INTERNAL_SERVER_ERROR"));
    resultActions.andExpect(jsonPath("$.message").value("Bad credentials"));
  }

  private RegistrationRequest buildRegistrationRequest() {
    return RegistrationRequest.builder()
        .username("test-user")
        .password("test-password")
        .confirmPassword("test-password")
        .build();
  }

  private AuthenticationRequest buildAuthenticationRequest() {
    return RegistrationRequest.builder()
        .username("test-user")
        .password("test-password")
        .build();
  }
}
