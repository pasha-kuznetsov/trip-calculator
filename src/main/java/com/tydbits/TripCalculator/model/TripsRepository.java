package com.tydbits.TripCalculator.model;

import org.springframework.stereotype.Component;

import javax.validation.ValidationException;
import java.util.Collection;
import java.util.HashMap;

@Component
public class TripsRepository {

    private HashMap<Integer, Trip> trips;

    public TripsRepository() {
        this.trips = new HashMap<>();
    }

    public void clear() {
        trips.clear();
    }

    public Collection<Trip> list() {
        return trips.values();
    }

    public Trip get(int tripId) {
        return trips.get(tripId);
    }

    public Trip add(Trip trip) {
        int id = trips.size() + 1;
        trip.setId(id);
        trips.put(id, trip);
        return trip;
    }

    public Trip put(int id, Trip trip) {
        if (!trips.containsKey(id))
            throw new ValidationException("No such trip: " + id);
        trip.setId(id);
        trips.put(id, trip);
        return trip;
    }
}
