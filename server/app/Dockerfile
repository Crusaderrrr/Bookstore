FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests -Dcheckstyle.skip=true

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.war app.war

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.war"]
