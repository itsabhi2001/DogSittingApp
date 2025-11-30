package com.abhimanyu.dogsitting.backend.dto;

import com.abhimanyu.dogsitting.backend.model.PetSize;
import com.abhimanyu.dogsitting.backend.model.ServiceType;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class PriceEstimateRequest {

    @NotNull
    private ServiceType serviceType;

    @NotNull
    private PetSize petSize; // not used in price yet, but useful for future rules

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public PetSize getPetSize() {
        return petSize;
    }

    public void setPetSize(PetSize petSize) {
        this.petSize = petSize;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
