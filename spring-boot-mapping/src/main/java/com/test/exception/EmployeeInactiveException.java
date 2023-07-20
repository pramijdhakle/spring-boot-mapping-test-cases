package com.test.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EmployeeInactiveException extends Exception{

    public EmployeeInactiveException(String message){
        super(message);
    }
}
