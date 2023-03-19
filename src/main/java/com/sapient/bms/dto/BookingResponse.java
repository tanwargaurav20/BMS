package com.sapient.bms.dto;

import java.math.BigDecimal;

public class BookingResponse {
    private Long bookingId;
    private BigDecimal amount;
    private String errorMessage;

    public BookingResponse(Long bookingId, BigDecimal amount) {
        this.bookingId = bookingId;
        this.amount = amount;
    }

    public BookingResponse(Long bookingId, BigDecimal amount, String errorMessage) {
        this.bookingId = bookingId;
        this.amount = amount;
        this.errorMessage = errorMessage;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}