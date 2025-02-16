package org.example.booktrackerservice.exception;

public class BookWasDeleted extends RuntimeException {
    public BookWasDeleted(String message) {
        super(message);
    }
}
