package com.abhimanyu.dogsitting.backend.repository;

import com.abhimanyu.dogsitting.backend.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
