package com.joon.sunguard_api.global.exception;

public class DuplicateFavoriteException extends RuntimeException {
    public DuplicateFavoriteException(String message) {
        super(message);
    }
}
