package org.example.bookstorageservice.exception;

public class IsbnAlreadyExists extends RuntimeException {
    public IsbnAlreadyExists(String message) {
        super(message);
    }
}
