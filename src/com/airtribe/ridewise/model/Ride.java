package com.airtribe.ridewise.model;

public class Ride {

    private final String id;
    private final Rider rider;
    private Driver driver;
    private final double distance;
    private RideStatus status;
    private FareReceipt fareReceipt;

    public Ride(String id, Rider rider, double distance) {
        this.id = id;
        this.rider = rider;
        this.distance = distance;
        this.status = RideStatus.REQUESTED;
    }

    public String getId() {
        return id;
    }

    public Rider getRider() {
        return rider;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public double getDistance() {
        return distance;
    }

    public RideStatus getStatus() {
        return status;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }

    public FareReceipt getFareReceipt() {
        return fareReceipt;
    }

    public void setFareReceipt(FareReceipt fareReceipt) {
        this.fareReceipt = fareReceipt;
    }

    @Override
    public String toString() {
        String driverName = (driver == null) ? "unassigned" : driver.getName();
        String fareInfo = (fareReceipt == null) ? "" : String.format(", fare=%.2f", fareReceipt.getAmount());
        return String.format("Ride[id=%s, rider=%s, driver=%s, distance=%.1fkm, status=%s%s]",
                id, rider.getName(), driverName, distance, status, fareInfo);
    }
}
