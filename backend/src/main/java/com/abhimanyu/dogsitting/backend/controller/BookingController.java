package com.abhimanyu.dogsitting.backend.controller;

import com.abhimanyu.dogsitting.backend.dto.BookingRequest;
import com.abhimanyu.dogsitting.backend.dto.BookingResponse;
import com.abhimanyu.dogsitting.backend.service.BookingService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/bookings")
@CrossOrigin(origins = "*")
public class BookingController {
    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse create(@Valid @RequestBody BookingRequest request) {
        return service.createBooking(request);
    }

    @GetMapping
    public List<BookingResponse> getAll(){
        return service.getAllBookings();
    }

}
