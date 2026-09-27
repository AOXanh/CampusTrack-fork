package com.jabai.campustrack.globalerrorhandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;

@ControllerAdvice 
public class GlobalErrorExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class) 
    public ResponseEntity<Map<String,String>>global(MethodArgumentNotValidException manve){ 
        Map<String, String> error = new HashMap<>(); 
        error.put("message", manve.getAllErrors().get(0).getDefaultMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<Map<String,String>>usernotfound(EmailNotFoundException unfe){ 
        Map<String, String> error = new HashMap<>(); 
        error.put("message", unfe.getMessage()); 
        return  ResponseEntity.status(404).body(error); 
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<Map<String, String>>usernamealreadyexist(EmailAlreadyExistException unaee){ 
        Map<String,String> error = new HashMap<>(); 
        error.put("message", unaee.getMessage());
        return ResponseEntity.status(409).body(error); 
    }
    
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String,String>>invalidcredentials(InvalidCredentialsException ice){ 
        Map<String,String> error = new HashMap<>(); 
        error.put("message", ice.getMessage()); 
        return ResponseEntity.status(401).body(error); 
    }




}
