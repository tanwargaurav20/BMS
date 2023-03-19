package com.sapient.bms.service;

import com.sapient.bms.dto.ScreenDto;
import com.sapient.bms.entity.Movie;
import com.sapient.bms.entity.Screen;
import com.sapient.bms.entity.Seat;
import com.sapient.bms.entity.Theatre;
import com.sapient.bms.respository.ScreenRepository;
import com.sapient.bms.respository.TheatreRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScreenService {
    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private TheatreRepository theaterRepository;

    @Autowired
    private ModelMapper modelMapper;


//    public List<Screen> createScreen(List<ScreenDto> screenDtos) {
//        List<Screen> screens = new ArrayList<>();
//        for (ScreenDto screenDto : screenDtos) {
//            Screen screen = new Screen();
//            screen.setId(screenDto.getId());
//            List<Movie> movies = screenDto.getMovies().stream().map(dto -> modelMapper.map(dto, Movie.class)).collect(Collectors.toList());
//            screen.setMovies(movies);
//            List<Seat>  seats = screenDto.getSeats().stream().map(dto -> modelMapper.map(dto , Seat.class)).collect(Collectors.toList());
//            screen.setSeats(seats);
//            screen.setShowTime(screenDto.getShowTime());
//
////            Optional<Theatre> theater = theaterRepository.findById(screen.getTheater().getId());
////            if (theater.isPresent()) {
////                screen.setTheater(theater.get());
////            }
//            screens.add(screen);
//        }
//        return screens;
//    }

    public Screen createScreen(Screen screen) {
        return screenRepository.save(screen);
    }


    public List<Screen> getScreensByTheater(Long theaterId) {
        return screenRepository.findByTheaterId(theaterId);
    }

    public Optional<Screen> getScreenById(Long screenId) {
        return screenRepository.findById(screenId);
    }

    // Other methods for updating and deleting screens
}

