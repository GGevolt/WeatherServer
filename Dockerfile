FROM maven:3.9.11-eclipse-temurin-24 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn -B -e dependency:go-offline

COPY src ./src
RUN mvn -B -e clean package -DskipTests

FROM eclipse-temurin:24-jre

WORKDIR /app

COPY --from=build /app/target/WeatherServer-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/WeatherServer-0.0.1-SNAPSHOT.jar"]