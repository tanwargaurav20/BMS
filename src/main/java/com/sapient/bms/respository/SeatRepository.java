package com.sapient.bms.respository;

import com.sapient.bms.entity.Screen;
import com.sapient.bms.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    
    @Query("SELECT s FROM Seat s WHERE s.screen = :screen")
    List<Seat> findAllByScreen(@Param("screen") Screen screen);

    @Query("SELECT s FROM Seat s WHERE s.screen = :screen AND s.isAvailable = true")
    List<Seat> findAllAvailableByScreen(@Param("screen") Screen screen);
    
}
