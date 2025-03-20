FROM openjdk:21-jdk-alpine
LABEL authors="kobyv"
COPY target/basketball-api-1.0.0.jar basketball-api-1.0.0.jar
ENTRYPOINT ["java","-jar","/basketball-api-1.0.0.jar"]