package com.project.authservice.exception;

public class HandleUserAlreadyExists extends RuntimeException{
    public HandleUserAlreadyExists(String message) {
        super(message);
    }
}
