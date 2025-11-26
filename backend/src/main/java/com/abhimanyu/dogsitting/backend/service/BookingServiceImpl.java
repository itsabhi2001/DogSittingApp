package com.abhimanyu.dogsitting.backend.service;

import com.abhimanyu.dogsitting.backend.dto.BookingRequest;
import com.abhimanyu.dogsitting.backend.dto.BookingResponse;
import com.abhimanyu.dogsitting.backend.model.Booking;
import com.abhimanyu.dogsitting.backend.model.BookingStatus;
import com.abhimanyu.dogsitting.backend.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class BookingServiceImpl implements BookingService{
    private final BookingRepository repo;

    public BookingServiceImpl(BookingRepository repo) {
        this.repo = repo;
    }

    @Override
    public BookingResponse createBooking(BookingRequest request){
        Booking booking = new Booking();
        booking.setClientName(request.getClientName());
        booking.setClientEmail(request.getClientEmail());
        booking.setPetName(request.getPetName());
        booking.setPetSize(request.getPetSize());
        booking.setServiceType(request.getServiceType());
        booking.setStartDate(request.getStartDate());
        booking.setEndDate(request.getEndDate());
        booking.setNotes(request.getNotes());

        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());

        booking.setTotalPrice(calculatePrice(booking));

        Booking saved = repo.save(booking);
        return toResponse(saved);


    }
    private double calculatePrice(Booking booking){
        double basePerNight = switch (booking.getServiceType()) {
            case HOUSE_SITTING -> 55.0;
            case WALK -> 20.0;
            case DROP_IN -> 30.0;
        };

        LocalDate start = booking.getStartDate();
        LocalDate end = booking.getEndDate();
        long days = ChronoUnit.DAYS.between(start, end);
        if (days <= 0) {
            days = 1; // minimum 1 day
        }
        return basePerNight * days;
    }

    private BookingResponse toResponse(Booking booking){
        return new BookingResponse(
                booking.getId(),
                booking.getClientName(),
                booking.getClientEmail(),
                booking.getPetName(),
                booking.getPetSize(),
                booking.getServiceType(),
                booking.getStartDate(),
                booking.getEndDate(),
                booking.getNotes(),
                booking.getStatus(),
                booking.getTotalPrice(),
                booking.getCreatedAt()
        );
    }
}
