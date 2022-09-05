FROM openjdk:11

WORKDIR /app

COPY /home/circleci/project/target/APODViewer-1.0-SNAPSHOT.jar ./

EXPOSE 8083

ENTRYPOINT ["java","-jar","APODViewer-1.0-SNAPSHOT.jar"]
# Copy jar file
#COPY target/APODViewer-1.0-SNAPSHOT.jar ./

# Copy hidden environment file
# COPY ./.env ./

#EXPOSE 8082
#
#CMD ["java", "-jar", "APODViewer-1.0-SNAPSHOT.jar"]


