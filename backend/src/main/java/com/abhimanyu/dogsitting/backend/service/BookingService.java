package com.abhimanyu.dogsitting.backend.service;

import com.abhimanyu.dogsitting.backend.dto.BookingRequest;
import com.abhimanyu.dogsitting.backend.dto.BookingResponse;

public interface BookingService  {
    BookingResponse createBooking(BookingRequest request);
}
