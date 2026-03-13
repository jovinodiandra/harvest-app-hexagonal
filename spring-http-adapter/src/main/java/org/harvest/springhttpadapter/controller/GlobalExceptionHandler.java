package org.harvest.springhttpadapter.controller;

import org.harvest.shared.exception.AuthenticationException;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleException(AuthenticationException e) {
        BaseResponse<?> baseResponse = new BaseResponse<>();
        baseResponse.setMessage(e.getMessage());
        return ResponseEntity.status(401).body(baseResponse);
    }
}
