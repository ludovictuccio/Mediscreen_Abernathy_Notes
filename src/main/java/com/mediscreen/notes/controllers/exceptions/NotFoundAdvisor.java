package com.mediscreen.notes.controllers.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class NotFoundAdvisor {

    @ExceptionHandler({ DataNotFoundException.class })
    public ResponseEntity<Object> handleAll(final Exception ex,
            final WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND,
                ex.getLocalizedMessage(), "Error occurred");
        return new ResponseEntity<Object>(apiError, new HttpHeaders(),
                apiError.getStatus());
    }
}
