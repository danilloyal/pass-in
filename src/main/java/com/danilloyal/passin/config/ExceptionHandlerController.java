package com.danilloyal.passin.config;

import com.danilloyal.passin.domain.event.exception.EventNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity handleEventNotFound(){
        return ResponseEntity.notFound().build();
    }
}
