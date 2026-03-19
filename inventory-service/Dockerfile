FROM eclipse-temurin:21-jdk-alpine as builder
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY api-gateway/pom.xml api-gateway/
COPY users-service/pom.xml users-service/
COPY inventory-service/pom.xml inventory-service/

RUN ./mvnw -B dependency:go-offline

COPY inventory-service/src inventory-service/src
RUN ./mvnw -B clean package -pl inventory-service -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/inventory-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
