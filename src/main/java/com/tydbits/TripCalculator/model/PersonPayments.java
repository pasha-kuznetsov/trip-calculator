package com.tydbits.TripCalculator.model;

import java.math.BigDecimal;
import java.util.Objects;

public class PersonPayments {
    private final String from;
    private final String to;
    private final BigDecimal amount;

    public PersonPayments(String from, String to, BigDecimal amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() && equals((PersonPayments) o);
    }

    public boolean equals(PersonPayments o) {
        return Objects.equals(from, o.from) &&
                Objects.equals(to, o.to) &&
                Objects.equals(amount, o.amount);
    }

    @Override
    public String toString() {
        return from + " -> " + to + ": " + amount;
    }
}
