package com.abhimanyu.dogsitting.backend.dto;


import com.abhimanyu.dogsitting.backend.model.BookingStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateBookingStatusRequest {

    @NotNull
    private BookingStatus status;

    public BookingStatus getStatus(){
        return status;
    }
    public void setStatus(BookingStatus status){
        this.status = status;
    }
}
