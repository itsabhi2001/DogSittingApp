package com.abhimanyu.dogsitting.backend.dto;



import com.abhimanyu.dogsitting.backend.model.PetSize;
import com.abhimanyu.dogsitting.backend.model.ServiceType;
import java.time.LocalDate;

import jakarta.validation.constraints.*;


public class BookingRequest {

    @NotBlank
    private String clientName;

    @NotBlank
    @Email
    private String clientEmail;

    @NotBlank
    private String petName;

    @NotNull
    private PetSize petSize;

    @NotNull
    private ServiceType serviceType;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private String notes;

    public BookingRequest() {
    }

    //Getters and Setters

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getClientEmail() { return clientEmail; }
    public void setClientEmail(String clientEmail) { this.clientEmail = clientEmail; }

    public String getPetName() { return petName; }
    public void setPetName(String petName) { this.petName = petName; }

    public PetSize getPetSize() { return petSize; }
    public void setPetSize(PetSize petSize) { this.petSize = petSize; }

    public ServiceType getServiceType() { return serviceType; }
    public void setServiceType(ServiceType serviceType) { this.serviceType = serviceType; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }


}
