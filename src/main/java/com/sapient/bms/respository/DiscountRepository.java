package com.sapient.bms.respository;

import com.sapient.bms.entity.Booking;
import com.sapient.bms.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    List<Discount> findByDiscountCodeAndActive(String discountCode, boolean isActive);
}