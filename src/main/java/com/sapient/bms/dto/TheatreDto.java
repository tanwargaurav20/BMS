package com.sapient.bms.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class TheatreDto {
    private long id;
    private String name;

    private LocationDto location;

    @Valid
    @NotEmpty(message = "At least one screen is required")
    private List<ScreenDto> screens;

    public TheatreDto() {
    }

    public TheatreDto(String name, LocationDto location, List<ScreenDto> screens) {
        this.name = name;
        this.location = location;
        this.screens = screens;
    }

    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocationDto getLocation() {
        return location;
    }

    public void setLocation(LocationDto location) {
        this.location = location;
    }

    public List<ScreenDto> getScreens() {
        return screens;
    }

    public void setScreens(List<ScreenDto> screens) {
        this.screens = screens;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
