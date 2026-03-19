FROM eclipse-temurin:21-jdk-alpine as builder
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
# Dar permisos de ejecución
RUN chmod +x mvnw
COPY api-gateway/pom.xml api-gateway/
COPY users-service/pom.xml users-service/
COPY inventory-service/pom.xml inventory-service/

RUN ./mvnw -B dependency:go-offline

COPY users-service/src users-service/src
RUN ./mvnw -B clean package -pl users-service -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/users-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
