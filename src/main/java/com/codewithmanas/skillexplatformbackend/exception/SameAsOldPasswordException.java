package com.codewithmanas.skillexplatformbackend.exception;

public class SameAsOldPasswordException extends RuntimeException {
    public SameAsOldPasswordException(String message) {
        super(message);
    }
}
