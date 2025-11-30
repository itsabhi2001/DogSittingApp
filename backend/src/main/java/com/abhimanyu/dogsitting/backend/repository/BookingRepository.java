package com.abhimanyu.dogsitting.backend.repository;

import com.abhimanyu.dogsitting.backend.model.Booking;
import com.abhimanyu.dogsitting.backend.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByStatus(BookingStatus status);

    // 🔹 Filter by client email (case-insensitive)
    List<Booking> findByClientEmailIgnoreCase(String clientEmail);

    // 🔹 Filter by both status AND email
    List<Booking> findByStatusAndClientEmailIgnoreCase(BookingStatus status, String clientEmail);

    long countByStatus(BookingStatus status);

}
