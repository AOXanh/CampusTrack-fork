package com.jabai.campustrack.Exceptions.CustomExceptions;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message){ 
        super(message); 
    }
}
