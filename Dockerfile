FROM maven:3.9.9-amazoncorretto-21 AS build
COPY . .

RUN mvn clean package -DskipTests

FROM amazoncorretto:21-alpine
COPY --from=build /target/pawmodoro-backend-0.0.1-SNAPSHOT.jar pawmodoro-backend.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "pawmodoro-backend.jar"]
