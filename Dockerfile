FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/testvoe_zadanie-0.0.1-SNAPSHOT.jar testvoe_zadanie.jar
ENV SPRING_PROFILES_ACTIVE=docker
CMD ["java", "-jar", "testvoe_zadanie.jar"]
