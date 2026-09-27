package com.jabai.campustrack.Exceptions.CustomExceptions;

public class EmailAlreadyExistException extends RuntimeException {
    public EmailAlreadyExistException(String message){ 
        super(message); 
    }
}
