package com.tradedoubler.authenticationservice.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response {

  @Builder.Default
  @JsonProperty(value = "status")
  private boolean status = true;

  @JsonProperty(value = "data")
  private Object data;

  public static Response from(Object data) {
    return Response.builder()
        .status(true)
        .data(data)
        .build();
  }
}
