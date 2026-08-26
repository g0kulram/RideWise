package com.airtribe.ridewise.service;

import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.util.IdGenerator;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class RiderService {

    private final Map<String, Rider> riders = new LinkedHashMap<>();

    public Rider registerRider(String name, Location location) {
        String id = IdGenerator.nextRiderId();
        Rider rider = new Rider(id, name, location);
        riders.put(id, rider);
        return rider;
    }

    public Optional<Rider> getRiderById(String id) {
        return Optional.ofNullable(riders.get(id));
    }

    public Collection<Rider> getAllRiders() {
        return riders.values();
    }
}
