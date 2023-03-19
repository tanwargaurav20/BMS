package com.sapient.bms.controller;

import com.sapient.bms.dto.DiscountDto;
import com.sapient.bms.entity.Discount;
import com.sapient.bms.service.DiscountService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/discount")
public class DiscountController {
    private final DiscountService discountService;
    private final ModelMapper modelMapper;

    public DiscountController(DiscountService discountService, ModelMapper modelMapper) {
        this.discountService = discountService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createDiscount(@RequestBody DiscountDto discountDto) {
        Discount discount = discountService.createDiscount(discountDto);
        return ResponseEntity.ok("Discount Created. Id" + discount.getId());
    }

}
