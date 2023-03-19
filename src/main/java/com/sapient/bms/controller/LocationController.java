package com.sapient.bms.controller;

import com.sapient.bms.dto.LocationDto;
import com.sapient.bms.dto.TheatreDto;
import com.sapient.bms.entity.Theatre;
import com.sapient.bms.service.LocationService;
import com.sapient.bms.service.TheatreService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/location")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }


    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createLocation(@Valid @RequestBody LocationDto locationDto,
                                                 UriComponentsBuilder uriBuilder) {
        LocationDto location = locationService.getOrCreateLocation(locationDto);
        UriComponents uriComponents = uriBuilder.path("/location/{id}")
                .buildAndExpand(location.getId());
        return ResponseEntity.created(uriComponents.toUri()).body("Location Created");
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<LocationDto>> getAllLocations() {
        return ResponseEntity.ok(locationService.getAllLocation());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<LocationDto> updateLocationById(@PathVariable Long id,
                                                          @Valid @RequestBody LocationDto requestDto) {
        return ResponseEntity.ok(locationService.updateLocation(id, requestDto));
    }

}
