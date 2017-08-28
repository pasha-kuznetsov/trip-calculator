package com.tydbits.TripCalculator.model;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class PersonExpenses {

    private final String personName;
    private final BigDecimal[] expenses;
    private BigDecimal total;

    public PersonExpenses(String personName, BigDecimal[] expenses) {
        this.personName = personName;
        this.expenses = expenses;
        this.total = null;
    }

    public String getPersonName() {
        return personName;
    }

    public BigDecimal[] getExpenses() {
        return expenses;
    }

    public BigDecimal getTotal() {
        if (total == null)
            total = Arrays.stream(expenses).reduce(BigDecimal.ZERO, BigDecimal::add);
        return total;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() && equals((PersonExpenses) o);
    }

    public boolean equals(PersonExpenses o) {
        return Objects.equals(personName, o.personName) &&
                Objects.equals(expenses, o.expenses);
    }

    @Override
    public String toString() {
        return personName + ": " +
                Arrays.stream(expenses)
                    .map(BigDecimal::toString)
                    .collect(Collectors.joining(", ", "[", "]"));
    }
}
