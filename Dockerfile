# Build Stage
FROM maven:3.9.5-openjdk-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Run Stage
FROM openjdk:21-jdk
WORKDIR /app
# Correctly copy the JAR file from the build stage
COPY --from=build /app/target/PHM_Demo-0.0.1-SNAPSHOT.jar phm_demo.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "phm_demo.jar"]
