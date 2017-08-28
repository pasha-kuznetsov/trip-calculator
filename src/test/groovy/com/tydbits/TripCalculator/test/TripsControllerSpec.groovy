package com.tydbits.TripCalculator.test

import com.tydbits.TripCalculator.api.TripsController
import com.tydbits.TripCalculator.model.TripsRepository
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import spock.lang.Specification
import spock.lang.Unroll

@WebMvcTest
@ContextConfiguration(classes = [TripsController.class, TripsRepository.class])
class TripsControllerSpec extends Specification {

    @Autowired
    TripsRepository repo

    @Autowired
    MockMvc mvc

    def setup() {
        repo.clear()
    }

    @Unroll
    def "post succeeds when #testCase"() {
        expect:
        def response = post([expenses: expenses])
        response.getStatus() == 200
        parseJson(response.contentAsString).payments == expectedPayments

        where:
        testCase                                | expenses | expectedPayments
        "basic scenario"                        | [
                [personName: "Louis", expenses: [5.75, 35.00, 12.79]],
                [personName: "Carter", expenses: [12.00, 15.00, 23.23]],
                [personName: "David", expenses: [10.00, 20.00, 38.41, 45.00]]
        ]                                                  | [
                [from: "Carter", to: "David", amount: 22.16],
                [from: "Louis", to: "David", amount: 18.85]
        ]
    }

    @Unroll
    def "post validation fails when #testCase"() {
        when:
        def response = post([expenses: expenses])
        def responseString = response.contentAsString

        then:
        response.getStatus() == 400
        for (def error : errors)
            assert responseString.contains(error)

        where:
        testCase                  | expenses | errors

        "personName is missing"      | [
                [personName: "Louis", expenses: [5.75, 35.00, 12.79, 29.93, 29.93]],
                [/* !!! */ expenses: [10.00, 20.00, 45.00]]
        ]                                    | ["personName", "must not be blank"]

        "personName is null"      | [
                [personName: "Louis", expenses: [5.75, 35.00, 12.79, 29.93, 29.93]],
                [personName: null /* <--- !!! */, expenses: [10.00, 20.00, 45.00]]
        ]                                    | ["personName", "must not be blank"]

        "personName is empty"      | [
                [personName: "Louis", expenses: [5.75, 35.00, 12.79, 29.93, 29.93]],
                [personName: "" /* <--- !!! */, expenses: [10.00, 20.00, 45.00]]
        ] | ["personName", "must not be blank"]

        "personName is duplicated"      | [
                [personName: "Louis", expenses: [5.75, 35.00, 12.79]],
                [personName: "Doug", expenses: [10.00, 20.00, 45.00]],
                [personName: "Louis", expenses: [29.93, 29.93]]
        ] | ["personName", "must be unique"]

        "expenses is missing" | [
                [personName: "Louis", expenses: [5.75, 35.00, 12.79]],
                [personName: "David"]
        ] | ["expenses", "must not be null"]

        "expenses is null" | [
                [personName: "Louis", expenses: [5.75, 35.00, 12.79]],
                [personName: "David", expenses: null]
        ] | ["expenses", "must not be null"]

        "amount < 0"          | [
                [personName: "Louis", expenses: [5.75, 35.00, 12.79, 29.93, 29.93]],
                [personName: "David", expenses: [10.00, 20.00, -38.41 /* <--- !!! */, 45.00]]
        ] | ["validated property", "expenses"]
        // TODO: FIXME: ["must be greater than or equal to 0.00"]
        // Spring throws:
        // JSR-303 validated property 'expenses[1].expenses[1].<list element>' does not have a corresponding
        // accessor for Spring data binding - check your DataBinder's configuration

        "amount has cent fractions"          | [
                [personName: "Louis", expenses: [5.75, 12.79, 29.93, 29.93]],
                [personName: "David", expenses: [5.75, 10.001 /* <--- !!! */, 35.00]],
                [personName: "Carter", expenses: [12.00, 15.00, 23.23, 63.19]]
        ] | ["validated property", "expenses"]
        // TODO: FIXME: TODO ["numeric value out of bounds (<6 digits>.<2 digits> expected)"]
        // Spring throws:
        // JSR-303 validated property 'expenses[1].expenses[1].<list element>' does not have a corresponding
        // accessor for Spring data binding - check your DataBinder's configuration
    }

    @Unroll
    def "put updates payments when #testCase"() {
        given:
        def response = post(postContent)
        assert response.getStatus() == 200
        assert parseJson(response.contentAsString).id == 1

        when:
        response = put(putContent)

        then:
        response.getStatus() == 200
        parseJson(response.contentAsString).payments == expectedPayments

        where:
        testCase         | postContent | putContent | expectedPayments
        "basic scenario" | [
                expenses: [
                        [personName: "Louis", expenses: [5.75, 35.00]],
                        [personName: "Carter", expenses: [12.00, 15.00, 23.23]]
                ]]                     | [
                id      : 1,
                expenses: [
                        [personName: "Louis", expenses: [5.75, 35.00, 12.79]],
                        [personName: "Carter", expenses: [12.00, 15.00, 23.23]],
                        [personName: "David", expenses: [10.00, 20.00, 38.41, 45.00]]
                ]]                                  | [
                [from: "Carter", to: "David", amount: 22.16],
                [from: "Louis", to: "David", amount: 18.85]
        ]
    }

    @Unroll
    def "put throws when #testCase"() {
        given:
        def response = post(postContent)
        assert response.getStatus() == 200
        assert parseJson(response.contentAsString).id == 1

        when:
        response = put(putContent)

        then:
        response.getStatus() == 400
        for (String error : errors)
            assert response.contentAsString.contains(error)

        where:
        testCase         | postContent | putContent | errors
        "wrong id" | [
                expenses: [
                        [personName: "Louis", expenses: [5.75, 35.00]],
                        [personName: "Carter", expenses: [12.00, 15.00, 23.23]]
                ]]                     | [
                id      : 666, // <--- !!!
                expenses: [
                        [personName: "Louis", expenses: [5.75, 35.00, 12.79]],
                        [personName: "Carter", expenses: [12.00, 15.00, 23.23]],
                        [personName: "David", expenses: [10.00, 20.00, 38.41, 45.00]]
                ]]                                  | [
                "No such trip: 666"
        ]
    }

    def post(content) {
        return request(HttpMethod.POST, "/api/v1/trips", content)
    }

    def put(content) {
        return request(HttpMethod.PUT, "/api/v1/trips/" + content["id"], content)
    }

    def request(HttpMethod httpMethod, urlTemplate, content) {
        return mvc.perform(
                new MockHttpServletRequestBuilder(httpMethod, new URI(urlTemplate))
                        .contentType("application/json")
                        .accept("application/json")
                        .content(new JsonBuilder(content).toString()))
                .andReturn().response
    }

    private static Object parseJson(String str) {
        return str == null || str == "" ? null : new JsonSlurper().parseText(str)
    }

    @TestConfiguration
    static class Config {

        @Bean
        static MockMvcExceptionMessageControllerAdvice exceptionHandlerControllerAdvice() {
            return new MockMvcExceptionMessageControllerAdvice()
        }
    }
}
