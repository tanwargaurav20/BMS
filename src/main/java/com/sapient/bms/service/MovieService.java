package com.sapient.bms.service;

import com.sapient.bms.dto.LocationDto;
import com.sapient.bms.dto.MovieDTO;
import com.sapient.bms.entity.Location;
import com.sapient.bms.entity.Movie;
import com.sapient.bms.entity.Screen;
import com.sapient.bms.exception.BadRequestException;
import com.sapient.bms.respository.LocationRepository;
import com.sapient.bms.respository.MovieRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MovieService {

    private final MovieRepository movieRepository;

    private final ScreenService screenService;
    private final ModelMapper modelMapper;

    @Autowired
    public MovieService(MovieRepository movieRepository, ScreenService screenService, ModelMapper modelMapper) {
        this.movieRepository = movieRepository;
        this.screenService = screenService;
        this.modelMapper = modelMapper;
    }

    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }

    public List<MovieDTO> getAllMoviesByTitle(String movieTitle) {
        return movieRepository.findAll()
                .stream()
                .filter(movie -> movieTitle.equalsIgnoreCase(movie.getTitle()))
                .map(movie -> modelMapper.map(movie, MovieDTO.class))
                .collect(Collectors.toList());

    }

    public MovieDTO getOrCreateMovie(MovieDTO movieDTO) {
        Optional<Movie> movieOptional = movieRepository.findById(movieDTO.getId());
        if (!movieOptional.isPresent()) {
            Movie movie = modelMapper.map(movieDTO, Movie.class);
            return modelMapper.map(movieRepository.save(movie), MovieDTO.class);
        }
        return modelMapper.map(movieOptional.get(), MovieDTO.class);
    }

    @Transactional
    public Screen addMovie(Long screenId, MovieDTO movieDTO) {
        Screen screen = screenService.getScreenById(screenId).orElseThrow(() -> new RuntimeException("Screen not found"));

        Movie movie = new Movie();
        movie.setTitle(movieDTO.getTitle());
        movie.setDescription(movieDTO.getDescription());
        movie.setLanguage(movieDTO.getLanguage());
        movie.setGenre(movieDTO.getGenre());
        movie.setScreen(screen);
        movie.setDuration(movieDTO.getDuration());
        movie.setPrice(movieDTO.getPrice());
        movieRepository.save(movie);

        screen.getMovies().add(movie);
        screenService.createScreen(screen);
        return screen;
    }
}

