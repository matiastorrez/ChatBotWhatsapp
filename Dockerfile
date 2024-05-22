FROM openjdk:17-jdk-alpine AS build
ARG JAR_FILE=target/*.jar
COPY ./target/Ceramica-0.0.1-SNAPSHOT.jar app2.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app2.jar"]
