package com.abhimanyu.dogsitting.backend.service;

import com.abhimanyu.dogsitting.backend.dto.BookingRequest;
import com.abhimanyu.dogsitting.backend.dto.BookingResponse;
import com.abhimanyu.dogsitting.backend.model.Booking;
import com.abhimanyu.dogsitting.backend.model.BookingStatus;
import com.abhimanyu.dogsitting.backend.repository.BookingRepository;
import com.abhimanyu.dogsitting.backend.exception.BookingNotFoundException;
import com.abhimanyu.dogsitting.backend.dto.PriceEstimateRequest;
import com.abhimanyu.dogsitting.backend.dto.PriceEstimateResponse;
import com.abhimanyu.dogsitting.backend.dto.BookingStatsResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

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

    @Override
    public List<BookingResponse> getAllBookings(){
        return  repo.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public BookingResponse getBookingById(Long id){
        Booking booking = repo.findById(id)
            .orElseThrow(() -> new BookingNotFoundException(id));

        return toResponse(booking);
    }

    @Override
    @Transactional
    public BookingResponse updateBookingStatus(Long id, BookingStatus newStatus){
        Booking booking = repo.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));

        BookingStatus current = booking.getStatus();

        switch (current) {
            case PENDING -> {
                if (newStatus != BookingStatus.CONFIRMED && newStatus != BookingStatus.CANCELED) {
                    throw new IllegalStateException("PENDING bookings can only move to CONFIRMED or CANCELED.");
                }
            }
            case CONFIRMED -> {
                if (newStatus != BookingStatus.CANCELED) {
                    throw new IllegalStateException("CONFIRMED bookings can only be moved to CANCELED.");
                }
            }
            case CANCELED -> {
                throw new IllegalStateException("CANCELED bookings cannot change status.");
            }
        }



        booking.setStatus(newStatus);
        Booking saved = repo.save(booking);

        return toResponse(saved);
    }

    @Override
    public void deleteBooking(Long id){
        Booking booking = repo.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));

        repo.delete(booking);
    }

    @Override
    public BookingResponse updateBooking(Long id, BookingRequest request){
        Booking booking = repo.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));

        // Update editable fields
        booking.setClientName(request.getClientName());
        booking.setClientEmail(request.getClientEmail());
        booking.setPetName(request.getPetName());
        booking.setPetSize(request.getPetSize());
        booking.setServiceType(request.getServiceType());
        booking.setStartDate(request.getStartDate());
        booking.setEndDate(request.getEndDate());
        booking.setNotes(request.getNotes());

        // Keep status & createdAt as-is
        // Recalculate price based on possibly new dates/service type
        booking.setTotalPrice(calculatePrice(booking));

        Booking saved = repo.save(booking);
        return toResponse(saved);
    }

    @Override
    public List<BookingResponse> searchBookings(BookingStatus status, String clientEmail) {
        // normalize email: treat blank strings as null
        String email = (clientEmail != null && !clientEmail.isBlank()) ? clientEmail : null;

        List<Booking> bookings;

        if (status == null && email == null) {
            // no filters → return all
            bookings = repo.findAll();
        } else if (status != null && email == null) {
            // filter only by status
            bookings = repo.findByStatus(status);
        } else if (status == null) {
            // filter only by email
            bookings = repo.findByClientEmailIgnoreCase(email);
        } else {
            // both filters provided
            bookings = repo.findByStatusAndClientEmailIgnoreCase(status, email);
        }

        return bookings.stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public PriceEstimateResponse estimatePrice(PriceEstimateRequest request) {
        Booking temp = new Booking();
        temp.setServiceType(request.getServiceType());
        temp.setPetSize(request.getPetSize());
        temp.setStartDate(request.getStartDate());
        temp.setEndDate(request.getEndDate());

        double price = calculatePrice(temp);

        return new PriceEstimateResponse(price);
    }

    @Override
    public BookingStatsResponse getStats() {
        long total = repo.count();
        long pending = repo.countByStatus(BookingStatus.PENDING);
        long confirmed = repo.countByStatus(BookingStatus.CONFIRMED);
        long canceled = repo.countByStatus(BookingStatus.CANCELED);

        return new BookingStatsResponse(
                total,
                pending,
                confirmed,
                canceled
        );
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
