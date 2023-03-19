package com.sapient.bms.exception;

public class SeatNotAvailableException extends RuntimeException {

    public SeatNotAvailableException() {
    }

    public SeatNotAvailableException(String message) {
        super(message);
    }

    public SeatNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
