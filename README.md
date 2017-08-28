## Intro

This app calculates the expenses for a group of students who like to go on road trips.

The group agrees in advance to share expenses equally, but it is not practical to share every expense as it occurs.
Thus individuals in the group pay for particular things, such as meals, hotels, taxi rides, and plane tickets.
After the trip, each student's expenses are tallied and money is exchanged so that the net cost to each is the same,
to within one cent. In the past, this money exchange has been tedious and time consuming.

This app computes, from a list of students and their personal expenses from the trip,
the minimum amount of money that must change hands in order to equalize (within one cent)
all the students' costs.

## API

The API is available at [http://trip-calc-test.tydapps.com/api/v1](http://trip-calc-test.tydapps.com/api/v1)

Please see [http://trip-calc-test.tydapps.com/swagger-ui.html](http://trip-calc-test.tydapps.com/swagger-ui.html)
for the API reference.

### Submitting a Trip

So, for example, Louis, Carter, and David took a trip together;
Louis incurred expenses of $5.75, $35.00, and $12.79,
Carter paid out $12.00, $15.00, and $23.23, and
David covered $10.00, $20.00, $38.41, and $45.00:

    POST http://trip-calc-test.tydapps.com/api/v1/trips
    Content-Type: application/json
    Accept: application/json

    {
        "expenses": [
            {"personName": "Louis", "expenses": [5.75, 35.00, 12.79]},
            {"personName": "Carter", "expenses": [12.00, 15.00, 23.23]},
            {"personName": "David", "expenses": [10.00, 20.00, 38.41, 45.00]}
        ]
    }

The above example includes three students; however,
the API accepts any number of students.

### Updating submitted Trips

Submitted Trips can be updated via `PUT` requests as follows:

    PUT http://trip-calc-test.tydapps.com/api/v1/trips/123
    Content-Type: application/json
    Accept: application/json

    {
        "expenses": [
            {"personName": "Louis", "expenses": [5.75, 35.00, 12.79]},
            {"personName": "Carter", "expenses": [12.00, 15.00, 23.23]},
            {"personName": "David", "expenses": [10.00, 20.00, 38.41, 45.00]}
        ]
    }

### Example Response

Louis' total was $53.54, Carter's was $50.23, and David shelled out $113.41.
The total cost of the trip was thus $217.18, and thus equal shares would be $72.39 1/3 cents.
Therefore, Louis owes David $18.85, and Carter owes David $22.16:

    HTTP 200 OK
    Content-Type: application/json

    {
        "id": 123,
        "expenses": [...],
        "payments": [
            ["from": "Louis", "to": "David", "amount": 18.85],
            ["from": "Carter", "to": "David", "amount": 22.16]
        ]
    }

### Example Error Response

For example, here's what you should expect in case if you forgot to include the `personName`:

    HTTP 400 Bad Request
    Content-Type: application/json
    
    {
        "timestamp": 1503851731550,
        "status": 400,
        "error": "Bad Request",
        "exception": "org.springframework.web.bind.MethodArgumentNotValidException",
        "errors": [
            {
                "codes": [
                    "NotBlank.trip.expenses[0].personName",
                    "NotBlank.trip.expenses.personName",
                    "NotBlank.expenses[0].personName",
                    "NotBlank.expenses.personName",
                    "NotBlank.personName",
                    "NotBlank.java.lang.String",
                    "NotBlank"
                ],
                "arguments": [
                    {
                        "codes": [
                            "trip.expenses[0].personName",
                            "expenses[0].personName"
                        ],
                        "arguments": null,
                        "defaultMessage": "expenses[0].personName",
                        "code": "expenses[0].personName"
                    }
                ],
                "defaultMessage": "must not be blank",
                "objectName": "trip",
                "field": "expenses[0].personName",
                "rejectedValue": "",
                "bindingFailure": false,
                "code": "NotBlank"
            }
        ],
        "message": "Validation failed for object='trip'. Error count: 1",
        "path": "/api/v1/trips"
    }

## Development

### Setup

Prerequisites:

* Java 8
* Gradle

IntelliJ IDEA, one time initialization:

    ./gradlew cleanIdea idea

### Test first

Everything should be green:

    ./gradlew test

### Run

Use the below command to start the app and create the following local endpoints:

    ./gradlew bootRun
    
* API [http://localhost:8080/api/v1](http://localhost:8080/api/v1)
* Swagger UI [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
* Swagger JSON [http://localhost:8080/api-docs/v1](http://localhost:8080/api-docs/v1)

### Deploying to Amazon AWS Elastic Beanstalk

The following command builds the JAR suitable for Amazon AWS Elastic Beanstalk deployment:

    ./gradlew bootRepackage

After Gradle finishes building the application,
the JAR will be located in `build/libs/TripCalculator-1.0-SNAPSHOT.jar`.