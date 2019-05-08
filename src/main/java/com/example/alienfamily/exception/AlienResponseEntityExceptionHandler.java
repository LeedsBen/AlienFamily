package com.example.alienfamily.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Exception Handler - map exceptions to HTTP code
 */
@ControllerAdvice
@RestController
public class AlienResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Exception Handler for Alien Exceptions
     *
     * @param ae
     * @return
     */
    @ExceptionHandler(AlienException.class)
    public final ResponseEntity<String> handleAlienException(AlienException ae) {
        String msg = ae.getMessage();
        // Switching on message content, bit mucky, but meh!
        // TODO: Future enhancement, create a different Exception for 'not found' errors
        if (msg != null && (msg.contains("not exist") || msg.contains("not found"))) {
            // Error relates to not finding an alien, send 404
            return new ResponseEntity<String>(msg, HttpStatus.NOT_FOUND);
        }
        // Error is a result of a Bad Request
        return new ResponseEntity<String>(msg, HttpStatus.BAD_REQUEST);
    }
}
