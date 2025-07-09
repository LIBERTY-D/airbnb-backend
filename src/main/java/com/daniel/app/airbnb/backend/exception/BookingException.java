package com.daniel.app.airbnb.backend.exception;

public class BookingException extends RuntimeException {
    public BookingException(String msg) {
        super(msg);
    }
}
