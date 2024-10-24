# **Sleep Challenge**

The **Sleep Challenge** is a service built with Kotlin/Java and Spring Boot, designed to log and analyze users' sleep patterns. 

The service allows users to:
 - Store sleep logs
 - Retrieve the last night's sleep data 
 - Calculate 30-day sleep averages with several insights like mood frequency, average time in bed, and bedtimes.

## Table of Contents

- [Technologies](#technologies)
- [Setup Instructions](#setup-instructions)
- [APIs](#apis)
- [Validations](#validations)
- [Tickets](#tickets)
- [Pull Requests](#pull-requests)
- [Postman Collection](#postman-collection)
- [Improvement List](#improvement-list)
- [Need help?](#need-help)

## Technologies

- **Backend**: Spring Boot (Java + Kotlin)
- **Database**: PostgreSQL with Flyway for migrations
- **Build Tool**: Gradle
- **Testing**: JUnit 5, Mockito
- **Validation**: Javax Validation
- **Containerization**: Docker

## Setup Instructions
Dockerfiles are set up for your convenience for running the whole project. You will need docker and ports 5432 (Postgres) and 8080 (API).

To run everything, simply execute `docker-compose up`. To build and run, execute `docker-compose up --build`.


## APIs
### User Endpoints
- **POST /users:** Create a new user.
````
# Request:
  curl --location 'localhost:8080/users' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  "email": "tom.brady@example.com",
  "username": "tbrady"
  }'
  
# Response:
{
    "id": 3,
    "username": "tbrady",
    "email": "tom.brady@example.com"
}
```` 
- **GET /users:** Get all users
```` 
# Request:
curl --location 'localhost:8080/users'

# Response:
[
    {
        "id": 1,
        "username": "jdoe",
        "email": "john.doe@example.com"
    },
    {
        "id": 2,
        "username": "pmahomes",
        "email": "patrick.mahomes@example.com"
    },
    {
        "id": 3,
        "username": "tbrady",
        "email": "tom.brady@example.com"
    }
]
```` 
### Sleep Log Endpoints
- **POST /sleep-logs:** Create a new sleep log.
````
# Request:
curl --location 'localhost:8080/sleep-logs' \
--header 'Content-Type: application/json' \
--data '{
    "userId": 3,
    "sleepStart": "20:00",
    "sleepEnd": "05:00",
    "mood": "GOOD"
}'

# Response:
{
    "id": 4,
    "userId": 3,
    "sleepStart": "2024-10-23T20:00:00",
    "sleepEnd": "2024-10-24T05:00:00",
    "mood": "GOOD"
}
````
- **GET /sleep-logs/user/{id}:** Get all sleep information for a specific user
````
# Request:
curl --location 'localhost:8080/sleep-logs/user/3'

# Response:
[
    {
        "id": 4,
        "userId": 3,
        "sleepStart": "2024-10-23T20:00:00",
        "sleepEnd": "2024-10-24T05:00:00",
        "mood": "GOOD"
    },
    {
        "id": 5,
        "userId": 3,
        "sleepStart": "2024-10-22T21:00:00",
        "sleepEnd": "2024-10-23T06:00:00",
        "mood": "OK"
    }
]
````
- **GET /sleep-logs/{userId}/last-night:** Get sleep information about the last night for an user
````
# Request:
curl --location 'localhost:8080/sleep-logs/user/3/last-night'

# Response: 
[
    {
        "id": 4,
        "userId": 3,
        "sleepStart": "2024-10-23T20:00:00",
        "sleepEnd": "2024-10-24T05:00:00",
        "mood": "GOOD"
    }
]
````
- **GET /sleep-logs/users/{userId}/sleep/averages/last30days:** Get sleep averages for an user over the last 30 days
````
# Request:
curl --location 'localhost:8080/sleep-logs/users/3/sleep/averages/last30days'

# Response:
{
    "startDate": "2024-09-24T00:55:33",
    "endDate": "2024-10-24T00:55:33",
    "averageTotalTimeInBed": "09:00",
    "averageBedtime": "20:30",
    "averageWakeTime": "05:30",
    "moodFrequencies": {
        "OK": 1,
        "GOOD": 1
    }
}
````

## Validations
The Sleep Challenge has validations to ensure data integrity and avoid conflicts during the creation of users and sleep logs. 

The following rules apply to user creation and sleep log management:

### User Validations
 - **Unique Username or Email:**

   - When creating a new user, the system ensures that no two users can have the same username or email. 
This validation prevents duplication and enforces unique user identification.
   - Error Response: If a user with the same username or email already exists, a 400 Bad Request status is returned with a message indicating that the user already exists.

Example:
````
{
"status": 400,
"error": "The username=XXXXXX and email=XXXXXX must be unique"
}
````
### Sleep Log Validations
 - **Duplicate Sleep Log:**

   - A user cannot have overlapping sleep logs. 
The system prevents the addition of sleep logs that overlap with an existing log for the same user within the same time range. This prevents multiple entries for the same sleep period.
   - Error Response: If a sleep log overlaps with an existing one, a 400 Bad Request status is returned with an error message.
````
{
  "status": 400,
  "error": "The sleep hours were already registered in this range - userId=XXXX, start=XXXXXX, end=XXXXXX"
}
````

### Validation Annotations
Spring's javax.validation.constraints annotations are used for data validation at the DTO level.


### Global Exception Handling
Validation errors are captured globally, and the application responds with appropriate HTTP status codes (400 Bad Request) and detailed error messages, making it easy for API clients to understand what went wrong.

## Tickets
The development of the Sleep Challenge follows a ticketing system for tracking tasks and features. 

Below is an overview of the key tickets implemented in the project:
  
 - **SLP-006: Add read me file**
    - Creation of the *README* file.
 - **SLP-005: Add Validations for SleepLogs and User**
   - Implemented validations to ensure data integrity.
     - User Validations: 
       - A user with the same username or email cannot be added to the system.
     - SleepLog Validations:
       - A user cannot have overlapping sleep logs, preventing multiple logs within the same time range.
- **SLP-004: Get 30-Day Averages**
  - Implemented the logic to calculate and return sleep data averages over the last 30 days.
    - The response includes:
      - The date range for the 30-day period.
      - Average total time in bed, average bedtime, and average wake-up time.
      - Mood frequencies based on the user's self-reported mood after waking up.
- **SLP-003: Adjust POST /sleep-logs, Added Mood Enum & Implement GET Last Night's Sleep Log API**
  - Adjusted the POST /sleep-logs endpoint to support the Mood enum, allowing users to specify their mood after waking up.
  - Added a GET endpoint to retrieve the user's last night's sleep log, providing a convenient way to access the most recent sleep entry.
- **SLP-002: Implement Initial Services and Controllers for Basic Interactions**
  - Set up the foundational services and controllers for the API.
  - Included basic interactions such as creating and retrieving sleep logs and user data.
- **SLP-001: Set up Database Schema for Sleep Logger**
  - Defined the initial database schema using PostgreSQL.
  - Created tables for users and sleep logs with proper relationships and constraints.
  - Used Flyway for database migrations to ensure schema versioning and consistency.
 
## Pull Requests
All changes were merged from feature branch (e.g pcastro/SLP-001) to develop branch, please feel free to check it out [here](https://github.com/PauloCastro1991/SleepChallenge/pulls?q=is%3Apr+is%3Aclosed).

## Postman Collection

To easily test the Sleep Logger API endpoints, you can use the provided Postman collection. The collection contains all the necessary requests for testing the API, including creating sleep logs, fetching 30-day averages, and more.

#### How to use:
1. Download [Postman Collection](./SleepChallenge.postman_collection) from the root of this repository.
2. Open Postman and click on `Import` at the top.
3. Select the downloaded `.postman_collection` file.
4. You’ll now have access to all the API endpoints in Postman to interact with the Sleep Logger API.


## Improvement List

This section outlines potential enhancements to improve functionality, user experience, and maintainability of the sleep logger API project.

- **Extra Validations**
    - Ensure users provide valid emails and other necessary data by adding format checks and validations.

- **Additional APIs**
    - Allow users to find a specific user by their ID, or delete user accounts with new API endpoints.

- **Export Data API**
    - Let users download their sleep logs as a CSV or PDF file for easier analysis or personal records.

- **Swagger Integration**
    - Offer developers a live and interactive guide to test and understand the available API endpoints using Swagger.

- **Frontend Application**
    - Create a simple web or mobile interface that makes it easy for users to log sleep data and view analytics without using raw API requests.

- **Security**
    - Protect the API by requiring users to securely log in and access only the data they are allowed to, using security standards like token-based authentication.

- **Documentation**
    - Provide clear instructions and examples that explain how to use the API, making it easy for others to get started with the system.


## Need Help?
I'm available to assist with any questions or clarifications you may have regarding this project. Whether you're facing issues with setup, implementation, or have suggestions for improvement, feel free to reach out. I'm happy to help!

**[Paulo Castro]** - paulo_cvp@hotmail.com
