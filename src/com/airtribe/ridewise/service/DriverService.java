package com.airtribe.ridewise.service;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.VehicleType;
import com.airtribe.ridewise.util.IdGenerator;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DriverService {

    private final Map<String, Driver> drivers = new LinkedHashMap<>();

    public Driver registerDriver(String name, Location location, VehicleType vehicleType) {
        String id = IdGenerator.nextDriverId();
        Driver driver = new Driver(id, name, location, vehicleType);
        drivers.put(id, driver);
        return driver;
    }

    public Optional<Driver> getDriverById(String id) {
        return Optional.ofNullable(drivers.get(id));
    }

    public List<Driver> listAvailableDrivers() {
        return drivers.values().stream()
                .filter(Driver::isAvailable)
                .collect(Collectors.toList());
    }

    public void updateAvailability(String driverId, boolean available) {
        Driver driver = drivers.get(driverId);
        if (driver != null) {
            driver.setAvailable(available);
        }
    }

    public Collection<Driver> getAllDrivers() {
        return drivers.values();
    }
}
