FROM openjdk:21-jdk
LABEL authors="kobyv"
EXPOSE 8080
COPY target/basketball-api-1.0.0.jar basketball-api-1.0.0.jar
ENTRYPOINT ["java","-jar","/basketball-api-1.0.0.jar"]