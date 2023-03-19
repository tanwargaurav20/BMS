package com.sapient.bms.dto;

import javax.validation.constraints.NotBlank;

public class LocationDto {

    private Long id;

    private String address;
    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Country is required")
    private String country;

    public LocationDto() {
    }

    public LocationDto(Long id, String address, String city, String state, String country) {
        this.id = id;
        this.city = city;
        this.state = state;
        this.country = country;
        this.address = address;
    }

    // Getters and setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}