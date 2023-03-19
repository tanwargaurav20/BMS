package com.sapient.bms.controller;

import com.sapient.bms.dto.LocationDto;
import com.sapient.bms.dto.ScreenDto;
import com.sapient.bms.dto.SeatDto;
import com.sapient.bms.dto.TheatreDto;
import com.sapient.bms.entity.Theatre;
import com.sapient.bms.service.ReservationService;
import com.sapient.bms.service.SeatService;
import com.sapient.bms.service.TheatreService;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/theatre")
public class TheatreController {
    private final TheatreService theatreService;
    private final ReservationService reservationService;
    private final SeatService seatService;
    private ModelMapper modelMapper;

    public TheatreController(TheatreService theatreService, SeatService seatService, ReservationService reservationService, ModelMapper modelMapper) {
        this.theatreService = theatreService;
        this.seatService = seatService;
        this.reservationService = reservationService;
        this.modelMapper = modelMapper;
    }


    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createTheatre(@Valid @RequestBody TheatreDto requestDto,
                                                UriComponentsBuilder uriBuilder) {
        Theatre theatre = theatreService.createTheatre(requestDto);
        UriComponents uriComponents = uriBuilder.path("/theatres/{id}")
                .buildAndExpand(theatre.getId());
        return ResponseEntity.created(uriComponents.toUri()).body("Theater Created");
    }

    @GetMapping
    public ResponseEntity<List<Theatre>> getAllTheatres() {
        List<Theatre> theatreDtos = theatreService.getAllTheatres();
        return ResponseEntity.ok(theatreDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Theatre> getTheatreById(@PathVariable Long id) {
        Optional<Theatre> theatre = theatreService.getTheatreById(id);
        if (theatre.isPresent()) {
            return ResponseEntity.ok(theatre.get());
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/criteria")
    public List<TheatreDto> searchTheatres(@RequestParam("locationId") Long location,
                                           @RequestParam("movieTitle") String movieTitle,
                                           @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Theatre> theatres = theatreService.getTheatersMatchingCriteria(location, movieTitle, date.atStartOfDay());
        List<TheatreDto> theatreResponseDTOs = new ArrayList<>();
        for (Theatre theatre : theatres) {
            TheatreDto theatreResponseDTO = new TheatreDto();
            theatreResponseDTO.setId(theatre.getId());
            theatreResponseDTO.setName(theatre.getName());
            theatreResponseDTO.setLocation(modelMapper.map(theatre.getLocation(), LocationDto.class));
            theatreResponseDTO.setScreens(theatre.getScreens().stream()

                    .map(screen -> {
                        ScreenDto screenResponseDTO = new ScreenDto();
                        screenResponseDTO.setId(screen.getId());
                        screenResponseDTO.setShowTime(screen.getShowTime());
                        screenResponseDTO.setSeats(screen.getSeats().stream()
                                .map(entity -> modelMapper.map(entity, SeatDto.class)).collect(Collectors.toList()));
                        return screenResponseDTO;
                    })
                    .collect(Collectors.toList()));
            theatreResponseDTOs.add(theatreResponseDTO);
        }
        return theatreResponseDTOs;
    }

//    private List<SeatDto> getAvailableSeats(Screen screen, LocalDate date) {
//        List<Reservation> reservations = reservationService.getReservationsByScreenAndSeatInAndExpirationTimeAfter(screen, screen.getSeats(), LocalDateTime.now());
//        List<Seat> bookedSeats = reservations.stream()
//                .filter(reservation -> reservation.getExpirationTime().toLocalDate().equals(date))
//                .map(reservation -> reservation.getSeat())
//                .collect(Collectors.toList());
//        List<Seat> allSeats = seatService.getAllSeatsByScreen(screen);
//        List<Seat> availableSeats = allSeats.stream()
//                .filter(seat -> !bookedSeats.contains(seat))
//                .collect(Collectors.toList());
//        return availableSeats.stream()
//                .map(seat -> {
//                    SeatDto seatResponseDTO = new SeatDto();
//                    seatResponseDTO.setId(seat.getId());
//                    seatResponseDTO.setRow(seat.getRowNumber());
//                    seatResponseDTO.setNumber(seat.getNumber());
//                    return seatResponseDTO;
//                })
//                .collect(Collectors.toList());
//    }


//    @PutMapping("/{id}")
//    public ResponseEntity<Theatre> updateTheatreById(@PathVariable Long id,
//                                                         @Valid @RequestBody TheatreRequestDto requestDto) {
//        TheatreDto theatreDto = theatreService.updateTheatreById(id, requestDto);
//        return ResponseEntity.ok(theatreDto);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheatreById(@PathVariable Long id) {
        theatreService.deleteTheatre(id);
        return ResponseEntity.noContent().build();
    }
}
