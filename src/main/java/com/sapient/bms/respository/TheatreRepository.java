package com.sapient.bms.respository;

import com.sapient.bms.entity.Location;
import com.sapient.bms.entity.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, Long> {

    List<Theatre> findByLocationAndScreens_Movies_IdAndScreens_ShowTimeAfter(Location location, Long movieId, LocalDateTime showTime);

    List<Theatre> findByLocationAndScreens_Movies_TitleAndScreens_ShowTimeAfter(Location location, String title, LocalDateTime showTime);
}
