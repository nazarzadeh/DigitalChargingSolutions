## Introduction
This project provides a REST API for managing Charge Detail Records (CDR) in real-time for a network of Charge Point Operators (CPO) in the context of an e-mobility charging solutions platform. The API provides three endpoints for creating, retrieving, and searching CDRs. The Controller layer manages the endpoints and requests, while the Service layer is responsible for the business logic of the application.

The API provides three endpoints for creating, retrieving, and searching CDRs.
## Technologies
* Spring Boot
* Java 17
* Gradle
* MongoDB
* Docker

## How to run the project
### Prerequisites
From the root directory of the project, run the following command to start the MongoDB container, docker daemon must be running:
```
./run.sh
```
Now you can run the project using the following command:
```
./gradlew bootRun
```
And also tests can be run using the following command:
```
./gradlew test
```
## Endpoints
This project has three endpoints as follows:

### Create a Charge Detail Record
This endpoint is responsible for creating a charge detail record. The following validations are made before creating the CDR:

* End time cannot be before start time
* Start time of an upcoming CDR for a particular vehicle must always be after the end time of any previous CDR
* Total cost must be greater than 0
### Get a Charge Detail Record by ID
This endpoint is responsible for retrieving a CDR by its ID.

### Search all Charge Detail Records for a particular vehicle
This endpoint is responsible for retrieving all CDRs for a particular vehicle. This endpoint accepts an optional "sortBy" parameter for specifying the field to sort the results on and an optional "sortDirection" parameter to specify the sort direction (either ASC or DESC).

## Service Layer
The Service layer of this project is responsible for the business logic of the application.

### Create a Charge Detail Record
This method is responsible for creating a CDR. It validates the input data and saves the record to the repository.

### Get a Charge Detail Record by ID
This method retrieves a CDR by its ID. If no CDR is found with the specified ID, it throws a RuntimeException.

### Search all Charge Detail Records for a particular vehicle
This method retrieves all CDRs for a particular vehicle. It accepts an optional "sortBy" parameter for specifying the field to sort the results on and an optional "sortDirection" parameter to specify the sort direction (either ASC or DESC).
## Architecture and Design:
This project uses a simple layered architecture with the following layers:

* Controller Layer: This layer is responsible for handling incoming HTTP requests and returning HTTP responses. It is implemented using Spring MVC.

* Service Layer: This layer contains the business logic of the application. It is responsible for performing validations, processing data, and interacting with the database. It is implemented using Java and Spring Framework.

* Repository Layer: This layer is responsible for interacting with the database. It provides methods for performing CRUD (Create, Read, Update, Delete) operations on the database. It is implemented using Spring Data JPA.

* Model Layer: This layer contains the data models used by the application. It is implemented using Java.

* Executor and CompletableFuture: In this project, Executor and CompletableFuture have been used to run the methods asynchronously.

* Exception Handling: The application has been designed to handle exceptions in a graceful manner. Appropriate HTTP status codes and error messages are returned to the client in case of exceptions.

* Logging: The application has been configured to log important information using slf4j.

* Unit Testing: The application has been designed with unit testing in mind.

