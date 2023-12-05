FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar sales.jar
ENTRYPOINT ["java","-jar","/sales.jar"]
EXPOSE 8080