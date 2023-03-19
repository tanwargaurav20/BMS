package com.sapient.bms.service;

import com.sapient.bms.dto.LocationDto;
import com.sapient.bms.entity.Location;
import com.sapient.bms.exception.BadRequestException;
import com.sapient.bms.respository.LocationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PromotionService {

    private final LocationRepository locationRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PromotionService(LocationRepository locationRepository, ModelMapper modelMapper) {
        this.locationRepository = locationRepository;
        this.modelMapper = modelMapper;
    }

    public LocationDto getLocationById(Long id) {
        Optional<Location> location = locationRepository.findById(id);
        return location.map(value -> modelMapper.map(value, LocationDto.class)).orElse(null);
    }

    public Optional<Location> getLocationEntityById(Long id) {
        return locationRepository.findById(id);
    }


    public List<LocationDto> getAllLocation() {
        return locationRepository.findAll()
                .stream()
                .map(location -> modelMapper.map(location, LocationDto.class))
                .collect(Collectors.toList());

    }

    public LocationDto createLocation(LocationDto locationDto) {
        Location location = modelMapper.map(locationDto, Location.class);
        return modelMapper.map(locationRepository.save(location), LocationDto.class);
    }

    public LocationDto getOrCreateLocation(LocationDto locationDto) {
        Optional<Location> locationOptional = locationRepository.findById(locationDto.getId());
        if (!locationOptional.isPresent()) {
            Location locationEntity = modelMapper.map(locationDto, Location.class);
            return modelMapper.map(locationRepository.save(locationEntity), LocationDto.class);
        }
        return modelMapper.map(locationOptional.get(), LocationDto.class);
    }

    public Location getOrCreateLocationEntity(LocationDto locationDto) {
        Optional<Location> locationOptional = locationRepository.findById(locationDto.getId());
        if (!locationOptional.isPresent()) {
            Location locationEntity = modelMapper.map(locationDto, Location.class);
            return locationRepository.save(locationEntity);
        }
        return locationOptional.get();
    }

    public LocationDto updateLocation(Long id, LocationDto locationDto) {
        Optional<Location> locationOptional = locationRepository.findById(locationDto.getId());
        if (locationOptional.isPresent()) {
            Location locationEntity = modelMapper.map(locationDto, Location.class);
            locationEntity.setId(id);
            return modelMapper.map(locationRepository.save(locationEntity), LocationDto.class);
        } else {
            throw new BadRequestException("Invalid Location");
        }
    }

}
