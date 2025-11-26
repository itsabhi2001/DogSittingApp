package com.abhimanyu.dogsitting.backend.dto;

import com.abhimanyu.dogsitting.backend.model.BookingStatus;
import com.abhimanyu.dogsitting.backend.model.PetSize;
import com.abhimanyu.dogsitting.backend.model.ServiceType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingResponse {
    private Long id;
    private String clientName;
    private String clientEmail;
    private String petName;
    private PetSize petSize;
    private ServiceType serviceType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String notes;
    private BookingStatus status;
    private Double totalPrice;
    private LocalDateTime createdAt;

    public BookingResponse() {
    }
    public BookingResponse(Long id,
                           String clientName,
                           String clientEmail,
                           String petName,
                           PetSize petSize,
                           ServiceType serviceType,
                           LocalDate startDate,
                           LocalDate endDate,
                           String notes,
                           BookingStatus status,
                           Double totalPrice,
                           LocalDateTime createdAt) {
        this.id = id;
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.petName = petName;
        this.petSize = petSize;
        this.serviceType = serviceType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
        this.status = status;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
    }

    //Getters and Setters
    public Long getId() { return id; }
    public String getClientName() { return clientName; }
    public String getClientEmail() { return clientEmail; }
    public String getPetName() { return petName; }
    public PetSize getPetSize() { return petSize; }
    public ServiceType getServiceType() { return serviceType; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public String getNotes() { return notes; }
    public BookingStatus getStatus() { return status; }
    public Double getTotalPrice() { return totalPrice; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setClientName(String clientName) { this.clientName = clientName; }
    public void setClientEmail(String clientEmail) { this.clientEmail = clientEmail; }
    public void setPetName(String petName) { this.petName = petName; }
    public void setPetSize(PetSize petSize) { this.petSize = petSize; }
    public void setServiceType(ServiceType serviceType) { this.serviceType = serviceType; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setStatus(BookingStatus status) { this.status = status; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
