package com.tydbits.TripCalculator.test

import com.tydbits.TripCalculator.model.Trip
import com.tydbits.TripCalculator.model.PersonExpenses
import com.tydbits.TripCalculator.model.PersonPayments
import spock.lang.Specification
import spock.lang.Unroll

class TripSpec extends Specification {

    @Unroll
    def "split #testCase"() {
        when:
        def trip = new Trip(expenses)

        def balance = new HashMap<String, BigDecimal>()
        expenses.each {
            balance[it.getPersonName()] = it.getTotal()
        }
        trip.getPayments().each {
            balance[it.getFrom()] = balance[it.getFrom()].subtract(it.amount)
            balance[it.getTo()] = balance[it.getTo()].add(it.amount)
        }

        then:
        balance.values().sum() == expenses.sum { it.getTotal() }
        trip.getPayments() == expectedPayments

        where:
        testCase                               | expenses | expectedPayments

        "empty input"                          | [
        ] as PersonExpenses[]                             | [
        ] as PersonPayments[]

        "1 student"                            | [
                ["Louis", [5.75, 35.00, 12.79, 29.93, 29.93] as BigDecimal[]]
        ] as PersonExpenses[]                             | [
        ] as PersonPayments[]

        "2 students, same to 1 cent"           | [
                ["Louis", [5.75, 35.00, 12.79, 29.93, 29.93] as BigDecimal[]],
                ["David", [10.00, 20.00, 38.41, 45.00] as BigDecimal[]]
        ] as PersonExpenses[]                             | [
        ] as PersonPayments[]

        "2 students, 1 nothing, 1 everything"  | [
                ["Louis", [] as BigDecimal[]],
                ["David", [10.00, 20.00, 38.41, 45.00] as BigDecimal[]]
        ] as PersonExpenses[]                             | [
                ["Louis", "David", 56.70 as BigDecimal]
        ] as PersonPayments[]

        "2 students, 1 below, 1 above average" | [
                ["Louis", [5.75, 35.00, 12.79] as BigDecimal[]],
                ["David", [10.00, 20.00, 38.41, 45.00] as BigDecimal[]]
        ] as PersonExpenses[]                             | [
                ["Louis", "David", 29.93 as BigDecimal]
        ] as PersonPayments[]

        "3 students: same to 1 cent"           | [
                ["Louis", [5.75, 35.00, 12.79, 59.88] as BigDecimal[]],
                ["Carter", [12.00, 15.00, 23.23, 63.19] as BigDecimal[]],
                ["David", [10.00, 20.00, 38.41, 45.00] as BigDecimal[]]
        ] as PersonExpenses[]                             | [
        ] as PersonPayments[]

        "3 students: 2 below, 1 above average" | [
                ["Louis", [5.75, 35.00, 12.79] as BigDecimal[]],
                ["Carter", [12.00, 15.00, 23.23] as BigDecimal[]],
                ["David", [10.00, 20.00, 38.41, 45.00] as BigDecimal[]]
        ] as PersonExpenses[]                             | [
                ["Carter", "David", 22.16 as BigDecimal],
                ["Louis", "David", 18.85 as BigDecimal]
        ] as PersonPayments[]

        "3 students, 1 below, 2 above average" | [
                ["Louis", [5.75, 35.00, 12.79] as BigDecimal[]],
                ["Carter", [12.00, 15.00, 23.23, 63.77] as BigDecimal[]],
                ["David", [10.00, 20.00, 38.41, 45.00] as BigDecimal[]]
        ] as PersonExpenses[]                             | [
                ["Louis", "David", 19.76 as BigDecimal],
                ["Louis", "Carter", 20.35 as BigDecimal]
        ] as PersonPayments[]
    }
}
