FROM openjdk:11

RUN mkdir /var/circleci-with-springboot

ARG DEPENDENCY=build
ADD ${DEPENDENCY}/libs/APODViewer-1.0-SNAPSHOT.jar /var/circleci-with-springboot/APODViewer-1.0-SNAPSHOT.jar

EXPOSE 8083

ENTRYPOINT ["java","-jar","/var/circleci-with-springboot/APODViewer-1.0-SNAPSHOT.jar"]
# Copy jar file
#COPY target/APODViewer-1.0-SNAPSHOT.jar ./

# Copy hidden environment file
# COPY ./.env ./

#EXPOSE 8082
#
#CMD ["java", "-jar", "APODViewer-1.0-SNAPSHOT.jar"]


