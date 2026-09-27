package com.jabai.campustrack.globalerrorhandler;

public class EmailAlreadyExistException extends RuntimeException {
    public EmailAlreadyExistException(String message){ 
        super(message); 
    }
}
