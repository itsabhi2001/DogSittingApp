package com.abhimanyu.dogsitting.backend.controller;

import com.abhimanyu.dogsitting.backend.dto.BookingRequest;
import com.abhimanyu.dogsitting.backend.dto.BookingResponse;
import com.abhimanyu.dogsitting.backend.dto.UpdateBookingStatusRequest;
import com.abhimanyu.dogsitting.backend.model.BookingStatus;
import com.abhimanyu.dogsitting.backend.service.BookingService;
import com.abhimanyu.dogsitting.backend.dto.PriceEstimateRequest;
import com.abhimanyu.dogsitting.backend.dto.PriceEstimateResponse;
import com.abhimanyu.dogsitting.backend.dto.BookingStatsResponse;


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
    public List<BookingResponse> search(
            @RequestParam(required = false) BookingStatus status,
            @RequestParam(required = false) String clientEmail
    ) {
        return service.searchBookings(status, clientEmail);
    }

    @GetMapping("/{id}")
    public BookingResponse getById(@PathVariable Long id) {
        return service.getBookingById(id);
    }

    @PatchMapping("/{id}/status")
    public BookingResponse updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateBookingStatusRequest request){
        BookingStatus updatedStatus = request.getStatus();
        return  service.updateBookingStatus(id, updatedStatus);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteBooking(id);
    }

    @PutMapping("/{id}")
    public BookingResponse update(@PathVariable Long id, @Valid @RequestBody BookingRequest request) {
        return service.updateBooking(id, request);
    }

    @PostMapping("/estimate")
    public PriceEstimateResponse estimate(
            @Valid @RequestBody PriceEstimateRequest request
    ) {
        return service.estimatePrice(request);
    }

    @GetMapping("/stats")
    public BookingStatsResponse getStats() {
        return service.getStats();
    }

}
