package com.sapient.bms.controller;

import com.sapient.bms.dto.BookingRequest;
import com.sapient.bms.dto.BookingResponse;
import com.sapient.bms.entity.Booking;
import com.sapient.bms.exception.MovieNotFoundException;
import com.sapient.bms.exception.PromotionNotFoundException;
import com.sapient.bms.exception.SeatNotAvailableException;
import com.sapient.bms.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> bookSeats(@RequestBody BookingRequest request) {
        try {
            Booking booking = bookingService.bookSeats(request);
            return ResponseEntity.ok(new BookingResponse(booking.getId(), booking.getAmount()));
        } catch (SeatNotAvailableException e) {
            return ResponseEntity.badRequest().body(new BookingResponse(null, BigDecimal.ZERO, e.getMessage()));
        } catch (MovieNotFoundException e) {
            return ResponseEntity.badRequest().body(new BookingResponse(null, BigDecimal.ZERO, "Movie not found"));
        } catch (PromotionNotFoundException e) {
            return ResponseEntity.badRequest().body(new BookingResponse(null, BigDecimal.ZERO, "Promotion not found"));
        }
    }

    @PutMapping("/{bookingId}/pay")
    public ResponseEntity<?> payBooking(@PathVariable Long bookingId) {
        bookingService.payBooking(bookingId);
        return ResponseEntity.ok().build();
    }

    // other methods
}
