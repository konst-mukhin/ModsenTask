package org.example.authservice.exception;

public class UsernameAlreadyInUse extends Exception {
    public UsernameAlreadyInUse(String message) {
        super(message);
    }
}
