package com.abhimanyu.dogsitting.backend.service;

import com.abhimanyu.dogsitting.backend.dto.*;
import com.abhimanyu.dogsitting.backend.model.BookingStatus;

import java.util.List;


public interface BookingService  {
    BookingResponse createBooking(BookingRequest request);

    List<BookingResponse> getAllBookings();

    BookingResponse getBookingById(Long id);

    BookingResponse updateBookingStatus(Long id, BookingStatus newStatus);

    void deleteBooking(Long id);

    BookingResponse updateBooking(Long id, BookingRequest request);

    List<BookingResponse> searchBookings(BookingStatus status, String clientEmail);

    PriceEstimateResponse estimatePrice(PriceEstimateRequest request);

    BookingStatsResponse getStats();

}
