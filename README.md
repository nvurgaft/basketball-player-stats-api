# Basketball API

This Spring Boot application serves as a RESTful API providing statistical data for basketball players. 
It allows users to query player performance metrics, such as points, assists, rebounds, and other key statistics. 
The API is built using Spring Web and integrates with a Postgresql database to retrieve and manage player data 
efficiently. 
It supports CRUD operations, user authentication, and data filtering based on various criteria like player name, team, 
and season. 
It has an aggregation API to retrieve aggregated statistical averages.  

## Features

This application tries to follow best practices for scalability and high availability and fault tolerance.

This application uses a Redis cache layer and query batching to improve data retrieval time and ease load on the database.

This application uses bean validation to validate requests bodies and parameters.

This application uses Swagger for API documentation.

This application has unit tests written with JUnit that utilize Testcontainers to spin Postgresql docker containers for 
E2E tests.

This application has Docker compatibility

This application uses Java 21 albeit older versions will probably work.
