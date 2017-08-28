package com.tydbits.TripCalculator.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class PersonExpenses {

    @NotBlank
    public String personName;

    @NotNull
    public List<
        @DecimalMin("0.00")
        @Digits(integer = 6, fraction = 2)
        BigDecimal> expenses;

    static PersonExpenses fromModel(com.tydbits.TripCalculator.model.PersonExpenses model) {
        PersonExpenses dto = new PersonExpenses();
        dto.personName = model.getPersonName();
        dto.expenses = Arrays.asList(model.getExpenses());
        return dto;
    }

    com.tydbits.TripCalculator.model.PersonExpenses toModel() {
        return new com.tydbits.TripCalculator.model.PersonExpenses(personName, expenses.toArray(new BigDecimal[expenses.size()]));
    }
}
