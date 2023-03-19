package com.sapient.bms.dto;

public class PromotionDto {

    private Long id;

    private int locationId;
    private int theatreId;
    private String promoCode;
    private int promoPercentage;

    private Boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getTheatreId() {
        return theatreId;
    }

    public void setTheatreId(int theatreId) {
        this.theatreId = theatreId;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public int getPromoPercentage() {
        return promoPercentage;
    }

    public void setPromoPercentage(int promoPercentage) {
        this.promoPercentage = promoPercentage;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}