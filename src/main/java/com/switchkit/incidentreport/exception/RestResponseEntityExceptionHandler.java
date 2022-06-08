package com.switchkit.incidentreport.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;


@ControllerAdvice
@ResponseStatus
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ExceptionResponse> authenticationException(AuthenticationFailedException ex, WebRequest request){

        ExceptionResponse message = new  ExceptionResponse(FORBIDDEN, ex.getMessage());

        return ResponseEntity.status(FORBIDDEN).body(message);

    }
    @ExceptionHandler(PendingAssignedTaskException.class)
    public ResponseEntity<ExceptionResponse> pendingAssignedTaskException(PendingAssignedTaskException ex,
                                                                          WebRequest request){
        ExceptionResponse message = new  ExceptionResponse(BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(message);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public  ResponseEntity<ExceptionResponse> recordNotFoundException(RecordNotFoundException ex,
                                                                      WebRequest request){
        ExceptionResponse message = new  ExceptionResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public  ResponseEntity<ExceptionResponse> illegalArgumentException(IllegalArgumentException ex, WebRequest request){
        ExceptionResponse message = new  ExceptionResponse(BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(message);
    }
    @ExceptionHandler(UsernameNameTakenException.class)
    public  ResponseEntity<ExceptionResponse> usernameNameTakenException(UsernameNameTakenException ex, WebRequest request){
        ExceptionResponse message = new  ExceptionResponse(BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(message);
    }
}
