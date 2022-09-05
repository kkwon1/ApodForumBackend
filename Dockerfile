FROM openjdk:14

WORKDIR /application

# Copy jar file
COPY out/artifacts/APODViewer_jar/APODViewer.jar ./

# Copy hidden environment file
COPY ./.env ./

EXPOSE 8082

CMD ["java", "-jar", "APODViewer.jar"]