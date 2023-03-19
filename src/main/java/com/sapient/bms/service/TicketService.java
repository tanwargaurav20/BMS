//package com.sapient.bookit.service;
//
//import com.sapient.bookit.entity.Screen;
//import com.sapient.bookit.entity.ShowTime;
//import com.sapient.bookit.entity.Ticket;
//import com.sapient.bookit.respository.ScreenRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class TicketService {
//
//    @Autowired
//    private TicketRepository ticketRepository;
//
//    @Autowired
//    private PromotionService promotionService;
//
//    @Autowired
//    private DiscountService discountService;
//
//    @Autowired
//    private ScreeningService screeningService;
//
//    @Autowired
//    private UserService userService;
//
//    public List<Ticket> bookTickets(Long screeningId, Long userId, List<Integer> seatNumbers) throws SeatNotAvailableException {
//        ShowTime screening = screeningService.getScreeningById(screeningId);
//        List<Ticket> tickets = new ArrayList<>();
//        List<Seat> reservedSeats = new ArrayList<>();
//        for(Integer seatNumber : seatNumbers) {
//            Seat seat = screening.getScreen().getSeatByNumber(seatNumber);
//            if(!seat.isAvailable()) {
//                throw new SeatNotAvailableException("Seat " + seatNumber + " is not available.");
//            }
//            seat.setAvailable(false);
//            reservedSeats.add(seat);
//        }
//        List<Promotion> promotions = promotionService.getPromotionsForScreening(screeningId);
//        List<Discount> discounts = discountService.getDiscountsForUser(userId);
//        for(Seat seat : reservedSeats) {
//            Ticket ticket = new Ticket(screening, seat, userService.getUserById(userId));
//            for(Promotion promotion : promotions) {
//                if(promotion.isApplicable(ticket)) {
//                    ticket.setPromotion(promotion);
//                    break;
//                }
//            }
//            for(Discount discount : discounts) {
//                if(discount.isApplicable(ticket)) {
//                    ticket.setDiscount(discount);
//                    break;
//                }
//            }
//            ticket = ticketRepository.save(ticket);
//            tickets.add(ticket);
//        }
//        // set a timer to unreserve the seats if not booked within 5 minutes
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                for(Seat seat : reservedSeats) {
//                    if(seat.isAvailable()) {
//                        continue;
//                    }
//                    Ticket ticket = ticketRepository.findByScreeningAndSeat(screening, seat);
//                    if(ticket == null) {
//                        continue;
//                    }
//                    if(!ticket.isBooked()) {
//                        seat.setAvailable(true);
//                        screening.getReservedSeats().remove(seat);
//                    }
//                }
//            }
//        }, 5 * 60 * 1000);
//        return tickets;
//    }
//}
