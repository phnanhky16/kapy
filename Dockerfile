FROM maven:3.9.11-amazoncorretto-21-debian AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn package -DskipTests -Dspotless.check.skip=true

FROM amazoncorretto:21.0.8

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]