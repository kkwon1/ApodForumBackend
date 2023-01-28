FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
ARG ENV_FILE=.env
COPY ${JAR_FILE} app.jar
COPY ${ENV_FILE} .env

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]