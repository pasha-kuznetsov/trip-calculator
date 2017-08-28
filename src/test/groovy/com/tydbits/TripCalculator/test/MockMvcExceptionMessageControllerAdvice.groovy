package com.tydbits.TripCalculator.test

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class MockMvcExceptionMessageControllerAdvice {

    @ExceptionHandler(Exception)
    public static ResponseEntity<String> handleException(Exception ex) {
        return new ResponseEntity<String>(ex.message, HttpStatus.BAD_REQUEST)
    }

}
