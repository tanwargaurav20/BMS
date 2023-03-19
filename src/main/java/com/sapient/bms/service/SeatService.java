package com.sapient.bms.service;

import com.sapient.bms.entity.Screen;
import com.sapient.bms.entity.Seat;
import com.sapient.bms.respository.SeatRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SeatService {

    private final SeatRepository seatRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public SeatService(SeatRepository seatRepository, ModelMapper modelMapper) {
        this.seatRepository = seatRepository;
        this.modelMapper = modelMapper;
    }

    public Optional<Seat> getSeatById(Long id) {
        return seatRepository.findById(id);

    }

    public List<Seat> getAllSeatsByScreen(Screen screen) {
        return seatRepository.findAllByScreen(screen);

    }

    public List<Seat> getAvailableSeatsByScreen(Screen screen) {
        return seatRepository.findAllAvailableByScreen(screen);

    }

    public Seat saveSeat(Seat seat) {
        return seatRepository.save(seat);
    }
}
