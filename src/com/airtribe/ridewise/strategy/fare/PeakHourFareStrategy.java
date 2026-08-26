package com.airtribe.ridewise.strategy.fare;

import com.airtribe.ridewise.model.Ride;

import java.time.LocalTime;

public class PeakHourFareStrategy implements FareStrategy {

    private static final double SURGE_MULTIPLIER = 1.5;
    private final FareStrategy baseStrategy;

    public PeakHourFareStrategy() {
        this(new DefaultFareStrategy());
    }

    public PeakHourFareStrategy(FareStrategy baseStrategy) {
        this.baseStrategy = baseStrategy;
    }

    @Override
    public double calculateFare(Ride ride) {
        double baseFare = baseStrategy.calculateFare(ride);
        return isPeakHour(LocalTime.now()) ? baseFare * SURGE_MULTIPLIER : baseFare;
    }

    private boolean isPeakHour(LocalTime time) {
        boolean morningPeak = !time.isBefore(LocalTime.of(8, 0)) && time.isBefore(LocalTime.of(11, 0));
        boolean eveningPeak = !time.isBefore(LocalTime.of(17, 0)) && time.isBefore(LocalTime.of(21, 0));
        return morningPeak || eveningPeak;
    }
}
