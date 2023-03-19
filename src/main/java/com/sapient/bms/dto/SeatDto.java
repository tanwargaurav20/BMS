package com.sapient.bms.dto;

public class SeatDto {
    private Long id;
    private int number;
    private int rowNumber;
    private boolean isAvailable;

    private SeatType seatType;

    public SeatDto() {
    }

    public SeatDto(int number, int rowNumber, SeatType seatType, boolean isAvailable) {
        this.number = number;
        this.rowNumber = rowNumber;
        this.seatType = seatType;
        this.isAvailable = isAvailable;
    }

    public int getNumber() {
        return number;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public void setSeatType(SeatType seatType) {
        this.seatType = seatType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
