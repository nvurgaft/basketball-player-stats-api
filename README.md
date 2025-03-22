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


## Deployment

This application has a `Dockerfile`, `docker-compose.yml` and a Kubernetes service descriptor (`kube.yaml`).

### Docker

TODO: add

### Minikube

Set `pwd` to the project root directory Build the Docker Image

    docker build -t nvurgaft/basketball-api:latest .

Push the Docker Image to a repository

    docker push nvurgaft/basketball-api

Start Minikube

    minikube start

Deploy the service

    kubectl apply -f kube.yaml 

You can verify the application is deployed using the Minikube Dashboard

First start the dashboard

    minikube dashboard

Than, use the browser to open up the UI

    http://127.0.0.1:39545/api/v1/namespaces/kubernetes-dashboard/services/http:kubernetes-dashboard:/proxy/