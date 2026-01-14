# Simple Dockerfile for Spring Boot (Maven, Java 17)
# This is enough to run the app on Render using Docker.

# ===== 1) Build stage: use Maven + Java 17 to build the jar =====
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml and download dependencies first (better layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the rest of the source code and build the jar
COPY src ./src
RUN mvn clean package -DskipTests

# ===== 2) Runtime stage: lightweight Java 17 image =====
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the built jar from the build stage
ARG JAR_FILE=target/*.jar
COPY --from=build /app/${JAR_FILE} app.jar

# Expose port 8080 (Spring Boot default)
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]

