package com.sapient.bms.controller;

import com.sapient.bms.dto.LocationDto;
import com.sapient.bms.dto.MovieDTO;
import com.sapient.bms.entity.Screen;
import com.sapient.bms.service.LocationService;
import com.sapient.bms.service.MovieService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieController {
    private final MovieService movieService;
    private final ModelMapper modelMapper;

    public MovieController(MovieService movieService, ModelMapper modelMapper) {
        this.movieService = movieService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createMovie(@RequestParam("screenId") Long screenId, @Valid @RequestBody MovieDTO movieRequestDto,
                                              UriComponentsBuilder uriBuilder) {
        Screen screen = movieService.addMovie(screenId, movieRequestDto);
        return ResponseEntity.ok("Movie Created. Screen Number" + screen.getNumber());
    }

    @GetMapping
    public ResponseEntity<List<MovieDTO>> getMovieByTitle(@RequestParam("title") String title) {
        return ResponseEntity.ok(movieService.getAllMoviesByTitle(title));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(modelMapper.map(movieService.getMovieById(id), MovieDTO.class));
    }


}
