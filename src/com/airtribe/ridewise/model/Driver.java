package com.airtribe.ridewise.model;

public class Driver {

    private final String id;
    private final String name;
    private Location currentLocation;
    private boolean available;
    private final VehicleType vehicleType;
    private int completedRideCount;

    public Driver(String id, String name, Location currentLocation, VehicleType vehicleType) {
        this.id = id;
        this.name = name;
        this.currentLocation = currentLocation;
        this.vehicleType = vehicleType;
        this.available = true;
        this.completedRideCount = 0;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public int getCompletedRideCount() {
        return completedRideCount;
    }

    public void incrementCompletedRides() {
        this.completedRideCount++;
    }

    @Override
    public String toString() {
        return String.format("Driver[id=%s, name=%s, vehicle=%s, available=%s, location=%s, completedRides=%d]",
                id, name, vehicleType, available, currentLocation, completedRideCount);
    }
}
