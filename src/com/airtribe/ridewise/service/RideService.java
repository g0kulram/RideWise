package com.airtribe.ridewise.service;

import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.FareReceipt;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.RideStatus;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.strategy.fare.FareStrategy;
import com.airtribe.ridewise.strategy.ridematching.RideMatchingStrategy;
import com.airtribe.ridewise.util.IdGenerator;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RideService {

    private final RiderService riderService;
    private final DriverService driverService;
    private RideMatchingStrategy matchingStrategy;
    private FareStrategy fareStrategy;

    private final Map<String, Ride> rides = new LinkedHashMap<>();

    public RideService(RiderService riderService,
                        DriverService driverService,
                        RideMatchingStrategy matchingStrategy,
                        FareStrategy fareStrategy) {
        this.riderService = riderService;
        this.driverService = driverService;
        this.matchingStrategy = matchingStrategy;
        this.fareStrategy = fareStrategy;
    }

    public void setMatchingStrategy(RideMatchingStrategy matchingStrategy) {
        this.matchingStrategy = matchingStrategy;
    }

    public void setFareStrategy(FareStrategy fareStrategy) {
        this.fareStrategy = fareStrategy;
    }

    public Ride requestRide(String riderId, double distance) {
        Rider rider = riderService.getRiderById(riderId)
                .orElseThrow(() -> new IllegalArgumentException("No rider found with id: " + riderId));

        List<Driver> availableDrivers = driverService.listAvailableDrivers();
        if (availableDrivers.isEmpty()) {
            throw new NoDriverAvailableException("No drivers are currently available.");
        }

        Driver matchedDriver = matchingStrategy.findDriver(rider, availableDrivers);
        if (matchedDriver == null) {
            throw new NoDriverAvailableException("Matching strategy could not find a suitable driver.");
        }

        String rideId = IdGenerator.nextRideId();
        Ride ride = new Ride(rideId, rider, distance);
        ride.setDriver(matchedDriver);
        ride.setStatus(RideStatus.ASSIGNED);

        driverService.updateAvailability(matchedDriver.getId(), false);
        rides.put(rideId, ride);
        return ride;
    }

    public Ride completeRide(String rideId) {
        Ride ride = getRideOrThrow(rideId);

        if (ride.getStatus() != RideStatus.ASSIGNED) {
            throw new IllegalStateException("Ride cannot be completed from status: " + ride.getStatus());
        }

        double fareAmount = fareStrategy.calculateFare(ride);
        FareReceipt receipt = new FareReceipt(ride.getId(), fareAmount, LocalDateTime.now());

        ride.setFareReceipt(receipt);
        ride.setStatus(RideStatus.COMPLETED);

        Driver driver = ride.getDriver();
        driver.incrementCompletedRides();
        driverService.updateAvailability(driver.getId(), true);

        return ride;
    }

    public Ride cancelRide(String rideId) {
        Ride ride = getRideOrThrow(rideId);

        if (ride.getStatus() == RideStatus.COMPLETED || ride.getStatus() == RideStatus.CANCELLED) {
            throw new IllegalStateException("Ride cannot be cancelled from status: " + ride.getStatus());
        }

        if (ride.getDriver() != null) {
            driverService.updateAvailability(ride.getDriver().getId(), true);
        }
        ride.setStatus(RideStatus.CANCELLED);
        return ride;
    }

    public Optional<Ride> getRideById(String rideId) {
        return Optional.ofNullable(rides.get(rideId));
    }

    public Collection<Ride> getAllRides() {
        return rides.values();
    }

    private Ride getRideOrThrow(String rideId) {
        return getRideById(rideId)
                .orElseThrow(() -> new IllegalArgumentException("No ride found with id: " + rideId));
    }
}
