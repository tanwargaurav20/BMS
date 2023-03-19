package com.sapient.bms.service;

import com.sapient.bms.dto.BookingRequest;
import com.sapient.bms.entity.*;
import com.sapient.bms.exception.*;
import com.sapient.bms.respository.BookingRepository;
import com.sapient.bms.service.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private SeatService seatService;

    @Autowired
    private ScreenService screenService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private DiscountService discountService;

    @Autowired
    private PromotionService promotionService;

    @Transactional
    public Booking bookSeats(BookingRequest request) throws ScreenNotAvailableException, SeatNotAvailableException, MovieNotFoundException, PromotionNotFoundException {
        Screen screen = screenService.getScreenById(request.getScreenId()).orElseThrow(() -> new ScreenNotAvailableException("Screen not found. Id: " + request.getScreenId()));
        Movie movie = movieService.getMovieById(request.getMovieId()).orElseThrow(() -> new MovieNotFoundException("Movie not found. Id: " + request.getMovieId()));

        List<Seat> availableSeats = seatService.getAvailableSeatsByScreen(screen);

        if (availableSeats.size() < request.getSeats().size()) {
            throw new SeatNotAvailableException("Requested seats are not available");
        }

        BigDecimal amount = movie.getPrice().multiply(BigDecimal.valueOf(request.getSeats().size()));

        if (request.getDiscountCode() != null ) {
            Discount discount = discountService.findDiscountByCode(request.getDiscountCode());
            if (discount != null) {
                if (discount.getMinQuantity() > 0) {
                    if (request.getSeats().size() >= discount.getMinQuantity()) {
                        amount = payableAmount(amount, discount.getDiscountPercentage());
                    }
                } else {
                    amount = payableAmount(amount, discount.getDiscountPercentage());
                }
            }
        }


        //Similarly promotion can be added as well
        // Not writing code for it as not in scope of this test


        Booking booking = new Booking();
        booking.setScreen(screen);
        booking.setMovie(movie);
        booking.setStartTime(LocalDateTime.now());
        booking.setAmount(amount);
        booking.setPaid(false);

        booking.setSeats(availableSeats);

        Reservation reservation = new Reservation();
        reservation.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        reservation.setUser(userService.getUserEntity(SecurityContextHolder.getContext().getAuthentication().getName()));
        reservation.setBooking(booking);
        booking.setReservation(reservation);

        for (Seat seat : availableSeats) {
            seat.setAvailable(false);
            seat.setReservedUntil(LocalDateTime.now().plusMinutes(15));
            seat.setBooking(booking);
            seatService.saveSeat(seat);
        }

        bookingRepository.save(booking);

        return booking;
    }

    @Transactional
    public void payBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BadRequestException("Booking with ID " + bookingId + " not found"));

        if (booking.getReservation().getExpiresAt().compareTo(LocalDateTime.now()) < 0) {
            throw new BadRequestException("Booking with ID " + bookingId + " has expired");
        }
        if (booking.isPaid()) {
            return;
        }

        // Make a call to the payment gateway with payment details like card details etc.
        // If successful update the paid flag to true
        booking.setPaid(true);
        booking.getSeats().stream().forEach(seat -> seat.setReservedUntil(null));

        bookingRepository.save(booking);

        Reservation reservation = booking.getReservation();
        if (reservation != null) {
            reservationService.deleteReservation(reservation);
        }
    }

    private BigDecimal payableAmount(BigDecimal amount, double discountPercentage) {
        return amount.multiply(BigDecimal.valueOf(100L)
                        .subtract(BigDecimal.valueOf(discountPercentage)))
                .divide(BigDecimal.valueOf(100L));
    }
}