# Use an official OpenJDK runtime (version 17) as a parent image
FROM openjdk:17-alpine3.14

#Add folder App in container
ADD . /app/

# Set the working directory to /app
WORKDIR /app

# Copy the compiled Spring Boot JAR file to the container
COPY target/microservice-diagnostic-0.0.1-SNAPSHOT.jar /app/microservice-diagnostic-0.0.1-SNAPSHOT.jar

# Specify the port number for the microservice
EXPOSE 9003

# Specify the command to run your Spring Boot app
CMD ["java", "-jar", "/app/microservice-diagnostic-0.0.1-SNAPSHOT.jar"]