# Fetch Java 17
FROM openjdk:17-jdk-alpine

# Copying over jar file and environment variables
ARG JAR_FILE=target/*.jar
ARG ENV_FILE=.env
COPY ${JAR_FILE} app.jar
COPY ${ENV_FILE} .env

# Expose port
EXPOSE 8080

# Starting the application
ENTRYPOINT ["java","-jar","/app.jar"]