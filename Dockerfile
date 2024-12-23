FROM jelastic/maven:3.9.5-openjdk-21 AS build
COPY . .
RUN mvn clean package -DskipTests
FROM openjdk:21-jdk
COPY --from=build /target/phm_demo-0.0.1-SNAPSHOT.jar phm_demo.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "phm_demo.jar"]