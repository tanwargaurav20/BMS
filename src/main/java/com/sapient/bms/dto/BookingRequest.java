package com.sapient.bms.dto;

import java.util.List;

public class BookingRequest {
    private Long screenId;
    private Long movieId;
    private List<SeatDto> seats;
    private String discountCode;
//    private PromotionDto promotion;


    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public List<SeatDto> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatDto> seats) {
        this.seats = seats;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscount(String discountCode) {
        this.discountCode = discountCode;
    }

//    public PromotionDto getPromotion() {
//        return promotion;
//    }
//
//    public void setPromotion(PromotionDto promotionDto) {
//        this.promotion = promotionDto;
//    }

    public Long getScreenId() {
        return screenId;
    }

    public void setScreenId(Long screenId) {
        this.screenId = screenId;
    }
}