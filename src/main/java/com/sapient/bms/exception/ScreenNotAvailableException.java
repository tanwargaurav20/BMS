package com.sapient.bms.exception;

public class ScreenNotAvailableException extends RuntimeException {

    public ScreenNotAvailableException() {
    }

    public ScreenNotAvailableException(String message) {
        super(message);
    }

    public ScreenNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
