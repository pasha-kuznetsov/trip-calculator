package com.tydbits.TripCalculator.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.tydbits.TripCalculator.dto.DtoUtils.mapToArray;
import static com.tydbits.TripCalculator.dto.DtoUtils.mapToList;

public class Trip {

    public int id;

    @NotNull
    public List<@Valid PersonExpenses> expenses;

    public List<PersonPayments> payments;

    public com.tydbits.TripCalculator.model.Trip toModel() {
        return new com.tydbits.TripCalculator.model.Trip(mapToArray(expenses, PersonExpenses::toModel, com.tydbits.TripCalculator.model.PersonExpenses[]::new));
    }

    public static Trip fromModel(com.tydbits.TripCalculator.model.Trip trip) {
        Trip dto = new Trip();
        dto.id = trip.getId();
        dto.expenses = mapToList(trip.getExpenses(), PersonExpenses::fromModel);
        dto.payments = mapToList(trip.getPayments(), PersonPayments::fromModel);
        return dto;
    }
}
