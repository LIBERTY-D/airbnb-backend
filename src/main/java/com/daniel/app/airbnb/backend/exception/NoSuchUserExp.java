package com.daniel.app.airbnb.backend.exception;

public class NoSuchUserExp extends RuntimeException {
    public NoSuchUserExp(String userCannotBeNull) {
        super(userCannotBeNull);
    }
}
