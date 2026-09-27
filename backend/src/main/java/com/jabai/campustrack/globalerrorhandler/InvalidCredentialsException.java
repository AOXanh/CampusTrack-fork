package com.jabai.campustrack.globalerrorhandler;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message){ 
        super(message); 
    }
}
