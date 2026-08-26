package com.airtribe.ridewise.strategy.fare;

import com.airtribe.ridewise.model.Ride;

public class DefaultFareStrategy implements FareStrategy {

    private static final double BASE_FARE = 30.0;
    private static final double RATE_PER_KM = 12.0;

    @Override
    public double calculateFare(Ride ride) {
        return BASE_FARE + (ride.getDistance() * RATE_PER_KM);
    }
}
