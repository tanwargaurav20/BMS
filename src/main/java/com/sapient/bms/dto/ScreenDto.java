package com.sapient.bms.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ScreenDto {
    private Long id;
    private int number;
    private int capacity;

    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss a")
    private LocalDateTime showTime;
    private List<MovieDTO> movies;
    private List<SeatDTO> seats;

    public ScreenDto() {
    }

    public ScreenDto(int number, int capacity, List<MovieDTO> movies, List<SeatDTO> seats) {
        this.number = number;
        this.capacity = capacity;
        this.movies = movies;
        this.seats = seats;
    }

    public int getNumber() {
        return number;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<MovieDTO> getMovies() {
        return movies;
    }

    public List<SeatDTO> getSeats() {
        return seats;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setMovie(List<MovieDTO> movies) {
        this.movies = movies;
    }

    public void setSeats(List<SeatDTO> seats) {
        this.seats = seats;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getShowTime() {
        return showTime;
    }

    public void setShowTime(LocalDateTime showTime) {
        this.showTime = showTime;
    }
}
