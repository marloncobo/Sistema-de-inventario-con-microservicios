FROM eclipse-temurin:21-jdk-alpine as builder
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN chmod +x mvnw
COPY api-gateway/pom.xml api-gateway/
COPY auth-service/pom.xml auth-service/
COPY inventory-service/pom.xml inventory-service/
COPY catalog-service/pom.xml catalog-service/
COPY sales-service/pom.xml sales-service/

RUN ./mvnw -B dependency:go-offline

COPY sales-service/src sales-service/src
RUN ./mvnw -B clean package -pl sales-service -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/sales-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
