package com.sapient.bms.respository;

import com.sapient.bms.entity.Reservation;
import com.sapient.bms.entity.Screen;
import com.sapient.bms.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

//    List<Reservation> findByScreenAndSeatInAndExpirationTimeAfter(Screen screen, List<Seat> seats, LocalDateTime expirationTime);

}
