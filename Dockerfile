FROM jelastic/maven:3.9.5-openjdk-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests
FROM openjdk:21-jdk
WORKDIR /app
COPY --from=build /app/target/PHM_Demo-0.0.1-SNAPSHOT.jar phm_demo.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "phm_demo.jar"]