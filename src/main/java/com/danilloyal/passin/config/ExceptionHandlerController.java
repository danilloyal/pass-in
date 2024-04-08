package com.danilloyal.passin.config;

import com.danilloyal.passin.domain.attendee.exception.AttendeeAlreadyRegisteredException;
import com.danilloyal.passin.domain.attendee.exception.AttendeeNotFoundException;
import com.danilloyal.passin.domain.check_in.exception.CheckInAlreadyExistsException;
import com.danilloyal.passin.domain.event.exception.EventFullException;
import com.danilloyal.passin.domain.event.exception.EventNotFoundException;
import com.danilloyal.passin.dto.general.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity handleEventNotFound(){
        return ResponseEntity.notFound().build();
    }
    @ExceptionHandler(EventFullException.class)
    public ResponseEntity<ErrorResponseDTO> handleEventFullException(EventFullException exception){
        return ResponseEntity.badRequest().body(new ErrorResponseDTO(exception.getMessage()));
    }
    @ExceptionHandler(AttendeeNotFoundException.class)
    public ResponseEntity handleAttendeeNotFound(){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AttendeeAlreadyRegisteredException.class)
    public ResponseEntity handleAttendeeAlreadyRegisteredException(){
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(CheckInAlreadyExistsException.class)
    public ResponseEntity handleCheckInAlreadyExistsException(){
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
