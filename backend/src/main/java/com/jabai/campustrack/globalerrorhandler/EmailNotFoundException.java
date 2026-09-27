package com.jabai.campustrack.globalerrorhandler;


public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException(String message){ 
        super(message); 
    }
}

