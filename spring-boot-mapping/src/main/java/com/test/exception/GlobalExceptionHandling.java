package com.test.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandling {

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorDetails> exceptionHandler(
            EmployeeNotFoundException employeeNotFoundException,
            WebRequest webRequest
    ){
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                employeeNotFoundException.getMessage(),
                webRequest.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = EmployeeInactiveException.class)
    public ResponseEntity<ErrorDetails> employeeInactiveExceptionHandler(
            EmployeeInactiveException employeeInactiveException,
            WebRequest webRequest
    ){
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                employeeInactiveException.getMessage(),
                webRequest.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
}
