package com.abhimanyu.dogsitting.backend.dto;

public class PriceEstimateResponse {

    private double estimatedPrice;

    public PriceEstimateResponse(double estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }

    public double getEstimatedPrice() {
        return estimatedPrice;
    }
}
