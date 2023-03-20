package com.sapient.bms.service;

import com.sapient.bms.dto.DiscountDto;
import com.sapient.bms.entity.Discount;
import com.sapient.bms.entity.Location;
import com.sapient.bms.entity.Theatre;
import com.sapient.bms.respository.DiscountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final LocationService locationService;
    private final TheatreService theatreService;
    private final ModelMapper modelMapper;

    @Autowired
    public DiscountService(DiscountRepository discountRepository, LocationService locationService, TheatreService theatreService, ModelMapper modelMapper) {
        this.locationService = locationService;
        this.theatreService = theatreService;
        this.discountRepository = discountRepository;
        this.modelMapper = modelMapper;
    }

    public Discount createDiscount(DiscountDto discountDto) {
        Location location = locationService.getLocationEntityById(discountDto.getLocationId()).orElseThrow(() -> new RuntimeException("Location not found. Id:" + discountDto.getLocationId()));
        Theatre theatre = theatreService.getTheatreById(discountDto.getTheatreId()).orElseThrow(() -> new RuntimeException("Theater not found. Id:" + discountDto.getTheatreId()));
        Discount discount = new Discount();
        discount.setLocation(location);
        discount.setTheatre(theatre);
        discount.setDiscountCode(discountDto.getDiscountCode());
        discount.setDiscountPercentage(discountDto.getDiscountPercentage());
        discount.setMinQuantity(discountDto.getMinQuantity());
        discount.setActive(discountDto.getActive());
        return discountRepository.save(discount);
    }

    public Discount findDiscountByCode(String discountCode) {
        return discountRepository.findByDiscountCodeAndActive(discountCode, true).get(0);
    }

    public Discount findByDiscountCodeAndLocationAndTheatreAndActive(String discountCode, Location location, Theatre theatre) {
        return discountRepository.findByDiscountCodeAndLocationAndTheatreAndActive(discountCode, location, theatre, true).get(0);
    }

}
