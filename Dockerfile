# Build stage
FROM maven:3.9.e-eclipse-temurin-17-a1pine AS build
COPY . .
RUN mvn clean package -DskipTests
# Package stage
FROM openjdk:19
COPY --from=build /target/Sales-0.0.1-SNAPSHOT.jar Sales-0.0.1-SNAPSHOT.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "Sales-0.0.1-SNAPSHOT.jar"]