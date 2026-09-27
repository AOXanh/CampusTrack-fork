package com.jabai.campustrack.Exceptions.CustomExceptions;


public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException(String message){ 
        super(message); 
    }
}

