package com.airtribe.ridewise.model;

import java.time.LocalDateTime;

public class FareReceipt {

    private final String rideId;
    private final double amount;
    private final LocalDateTime generatedAt;

    public FareReceipt(String rideId, double amount, LocalDateTime generatedAt) {
        this.rideId = rideId;
        this.amount = amount;
        this.generatedAt = generatedAt;
    }

    public String getRideId() {
        return rideId;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    @Override
    public String toString() {
        return String.format("FareReceipt[rideId=%s, amount=%.2f, generatedAt=%s]", rideId, amount, generatedAt);
    }
}
