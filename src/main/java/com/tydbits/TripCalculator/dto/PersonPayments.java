package com.tydbits.TripCalculator.dto;

import java.math.BigDecimal;

public class PersonPayments {

    public String from;
    public String to;
    public BigDecimal amount;

    static PersonPayments fromModel(com.tydbits.TripCalculator.model.PersonPayments model) {
        PersonPayments dto = new PersonPayments();
        dto.from = model.getFrom();
        dto.to = model.getTo();
        dto.amount = model.getAmount();
        return dto;
    }
}
