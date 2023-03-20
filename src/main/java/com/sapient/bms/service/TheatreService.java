package com.sapient.bms.service;

import com.sapient.bms.dto.ScreenDto;
import com.sapient.bms.dto.TheatreDto;
import com.sapient.bms.entity.Location;
import com.sapient.bms.entity.Screen;
import com.sapient.bms.entity.Seat;
import com.sapient.bms.entity.Theatre;
import com.sapient.bms.exception.BadRequestException;
import com.sapient.bms.respository.TheatreRepository;
import com.sapient.bms.service.auth.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TheatreService {
    private final TheatreRepository theatreRepository;
    private final LocationService locationService;
    private final ScreenService screenService;

    private final SeatService seatService;

    private final UserService userService;

    private final ModelMapper modelMapper;

    @Autowired
    public TheatreService(TheatreRepository theatreRepository, LocationService locationService, ScreenService screenService, SeatService seatService, UserService userService, ModelMapper modelMapper) {
        this.theatreRepository = theatreRepository;
        this.locationService = locationService;
        this.screenService = screenService;
        this.seatService = seatService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public Theatre createTheatre(TheatreDto theatreRequest) {
        Location location = locationService.getOrCreateLocationEntity(theatreRequest.getLocation());
        Theatre theatre = new Theatre();
        theatre.setName(theatreRequest.getName());
        theatre.setLocation(location);
        theatreRepository.save(theatre);

        for (ScreenDto screenDto : theatreRequest.getScreens()) {
            Screen screen = new Screen();
            screen.setNumber(screenDto.getNumber());
            screen.setTheater(theatre);
            screen.setShowTime(screenDto.getShowTime());
            screen.setCapacity(screenDto.getCapacity());
            screen = screenService.createScreen(screen);

            for (int i = 0; i < screenDto.getSeats().size(); i++) {
                Seat seat = new Seat();
                seat.setRowNumber(i);
                seat.setNumber(screenDto.getSeats().get(i).getNumber());
                seat.setScreen(screen);
                seat.setAvailable(screenDto.getSeats().get(i).isAvailable());
                seat.setType(screenDto.getSeats().get(i).getSeatType());
                seat.setUser(userService.getUserEntity(SecurityContextHolder.getContext().getAuthentication().getName()));
                seatService.saveSeat(seat);
            }
        }

        return theatre;

    }

    public List<Theatre> getAllTheatres() {
        return theatreRepository.findAll();
    }

    public Optional<Theatre> getTheatreById(Long id) {
        return theatreRepository.findById(id);
    }

    @Transactional
    public void deleteTheatre(Long id) {
        theatreRepository.deleteById(id);
    }

    public List<Theatre> getTheatersMatchingCriteria(Long locationId, String movieTitle, LocalDateTime showTime) {
        Location location = locationService.getLocationEntityById(locationId).orElseThrow(() -> new BadRequestException("Location doesn't exist. Id: " + locationId));
        return theatreRepository.findByLocationAndScreens_Movies_TitleAndScreens_ShowTimeAfter(location, movieTitle, showTime);
    }
}
