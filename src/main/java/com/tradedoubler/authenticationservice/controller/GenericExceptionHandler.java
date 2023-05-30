package com.tradedoubler.authenticationservice.controller;

import com.tradedoubler.authenticationservice.model.ApiError;
import io.micrometer.common.lang.Nullable;
import java.time.LocalDateTime;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GenericExceptionHandler extends ResponseEntityExceptionHandler {


  @Override
  protected ResponseEntity<Object> createResponseEntity(@Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
    String message = "No information";
    if (body instanceof ProblemDetail) {
      ProblemDetail problemDetail = ((ProblemDetail) body);
      message = problemDetail.getTitle();
    }
    ApiError err = new ApiError(
        LocalDateTime.now(),
        HttpStatus.valueOf(statusCode.value()),
        message);

    return new ResponseEntity<>(err, headers, statusCode);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Object> handleRuntimeException(
      RuntimeException ex) {

    ApiError err = new ApiError(
        LocalDateTime.now(),
        HttpStatus.INTERNAL_SERVER_ERROR,
        ex.getMessage());

    return build(err);
  }

  private ResponseEntity<Object> build(ApiError apiError) {
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }


}
