package md.ceiti.backend.advice;

import md.ceiti.backend.constant.ErrorCodes;
import md.ceiti.backend.exception.ApplicationException;
import md.ceiti.backend.exception.ExceptionResponse;
import md.ceiti.backend.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(ApplicationException.class)
    private ResponseEntity<ExceptionResponse> handleException(ApplicationException e) {
        return new ResponseEntity<>(
                new ExceptionResponse(e.getMessage(), e.getErrorCode(), null, new Date().getTime()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ValidationException.class)
    private ResponseEntity<ExceptionResponse> handleException(ValidationException e) {
        return new ResponseEntity<>(
                new ExceptionResponse(e.getMessage(),
                        ErrorCodes.VALIDATION_ERROR,
                        e.getErrors(),
                        new Date().getTime()),
                HttpStatus.BAD_REQUEST
        );
    }
}
