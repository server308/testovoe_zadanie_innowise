FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/testvoe_zadanie-0.0.1-SNAPSHOT.jar testvoe_zadanie.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "testvoe_zadanie.jar"]

