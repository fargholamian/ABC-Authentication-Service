package com.tradedoubler.authenticationservice.controller;

import com.tradedoubler.authenticationservice.utils.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/user/")
@RequiredArgsConstructor
public class UserController {

  @GetMapping
  public ResponseEntity<Response> getCurrentUser() {
    return ResponseEntity
        .ok()
        .body(Response.from(AuthenticationUtils.getCurrentUser()));
  }
}
