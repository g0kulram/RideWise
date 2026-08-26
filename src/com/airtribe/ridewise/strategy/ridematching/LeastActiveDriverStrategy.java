package com.airtribe.ridewise.strategy.ridematching;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Rider;

import java.util.List;

public class LeastActiveDriverStrategy implements RideMatchingStrategy {

    @Override
    public Driver findDriver(Rider rider, List<Driver> drivers) {
        Driver leastActive = null;
        int minCompleted = Integer.MAX_VALUE;

        for (Driver driver : drivers) {
            if (driver.getCompletedRideCount() < minCompleted) {
                minCompleted = driver.getCompletedRideCount();
                leastActive = driver;
            }
        }
        return leastActive;
    }
}
