FROM openjdk:11

WORKDIR /application

# Copy jar file
COPY target/APODViewer-1.0-SNAPSHOT.jar ./

# Copy hidden environment file
# COPY ./.env ./

EXPOSE 8082

CMD ["java", "-jar", "APODViewer-1.0-SNAPSHOT.jar"]