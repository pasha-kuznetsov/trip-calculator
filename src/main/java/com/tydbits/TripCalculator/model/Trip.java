package com.tydbits.TripCalculator.model;

import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

public class Trip {

    private int id;
    private HashMap<String, PersonExpenses> expenses = new HashMap<>();
    private BigDecimal average;
    private PersonPayments[] payments;

    public Trip(PersonExpenses[] expenses) {
        for (PersonExpenses x : expenses)
            if (this.expenses.put(x.getPersonName(), x) != null)
                throw new ValidationException("personName: must be unique");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Collection<PersonExpenses> getExpenses() {
        return expenses.values();
    }

    public PersonPayments[] getPayments() {
        if (payments == null)
            payments = calculatePayments();
        return payments;
    }

    private PersonPayments[] calculatePayments() {
        if (expenses.size() <= 1)
            return new PersonPayments[0];

        List<PersonPayments> payments = new ArrayList<>();
        Iterator<Balance> debits = debitsStream().iterator();
        Iterator<Balance> credits = creditsStream().iterator();
        Balance creditor = null;
        while (debits.hasNext()) {
            Balance debtor = debits.next();
            while (debtor.balance.compareTo(BigDecimal.ZERO) < 0) {
                if (creditor == null || creditor.balance.compareTo(BigDecimal.ZERO) <= 0)
                    creditor = credits.next();
                BigDecimal amount = min(debtor.balance.abs(), creditor.balance.abs());
                debtor.balance = debtor.balance.add(amount);
                creditor.balance = creditor.balance.subtract(amount);
                payments.add(new PersonPayments(debtor.personName, creditor.personName, amount));
            }
        }
        return payments.toArray(new PersonPayments[payments.size()]);
    }

    private Stream<Balance> debitsStream() {
        return expenses.values().stream().filter(this::lessThanAverage).map(this::balance);
    }

    private Stream<Balance> creditsStream() {
        return expenses.values().stream().filter(this::greaterThanAverage).map(this::balance);
    }

    private boolean lessThanAverage(PersonExpenses personExpenses) {
        return personExpenses.getTotal().compareTo(getAverage()) < 0;
    }

    private boolean greaterThanAverage(PersonExpenses personExpenses) {
        return personExpenses.getTotal().compareTo(getAverage()) > 0;
    }

    private BigDecimal min(BigDecimal l, BigDecimal r) {
        return l.compareTo(r) <= 0 ? l : r;
    }

    private Balance balance(PersonExpenses personExpenses) {
        String personName = personExpenses.getPersonName();
        BigDecimal total = personExpenses.getTotal();
        BigDecimal balance = total.subtract(getAverage());
        return new Balance(personName, balance);
    }

    private BigDecimal getAverage() {
        if (average == null)
            average = expenses.values().stream()
                    .map(PersonExpenses::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(new BigDecimal(expenses.size()), 2, BigDecimal.ROUND_DOWN);
        return average;
    }

    class Balance {
        final String personName;
        BigDecimal balance;

        Balance(String personName, BigDecimal balance) {
            this.personName = personName;
            this.balance = balance;
        }

        public String toString() {
            return personName + ": " + balance;
        }
    }
}
