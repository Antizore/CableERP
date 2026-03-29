FROM maven:3.9.11-eclipse-temurin-25 AS build
WORKDIR /home/app
COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /home/app/target/*.jar app.jar


EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]