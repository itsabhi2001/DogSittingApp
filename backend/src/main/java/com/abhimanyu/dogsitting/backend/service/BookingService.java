package com.abhimanyu.dogsitting.backend.service;

import com.abhimanyu.dogsitting.backend.dto.BookingRequest;
import com.abhimanyu.dogsitting.backend.dto.BookingResponse;
import java.util.List;


public interface BookingService  {
    BookingResponse createBooking(BookingRequest request);

    List<BookingResponse> getAllBookings();

}
