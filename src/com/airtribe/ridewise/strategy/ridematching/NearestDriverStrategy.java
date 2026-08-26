package com.airtribe.ridewise.strategy.ridematching;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Rider;

import java.util.List;

public class NearestDriverStrategy implements RideMatchingStrategy {

    @Override
    public Driver findDriver(Rider rider, List<Driver> drivers) {
        Driver nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (Driver driver : drivers) {
            double distance = rider.getLocation().distanceTo(driver.getCurrentLocation());
            if (distance < minDistance) {
                minDistance = distance;
                nearest = driver;
            }
        }
        return nearest;
    }
}
