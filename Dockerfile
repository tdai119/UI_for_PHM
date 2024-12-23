FROM jelastic/maven:3.9.5-openjdk-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests
FROM openjdk:21-jdk
WORKDIR /app
COPY --from=build /app/target/PHM_Demo-0.0.1-SNAPSHOT.jar phm_demo.jar
# Create the uploads directory and set permissions
RUN mkdir -p /app/uploads && chmod -R 777 /app/uploads
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "phm_demo.jar"]