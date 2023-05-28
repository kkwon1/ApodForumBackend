# ApodForumBackend (Java)

Link to APOD Forum application :ringed_planet: -> https://apod.kkwon.dev/

## Introduction

NASA offers an API for a daily media upload related to astronomy called [Astronomy Picture of the Day (APOD)](https://data.nasa.gov/Space-Science/Astronomy-Picture-of-the-Day-API/ez2w-t8ua).
- [NASA's official APOD Github](https://github.com/nasa/apod-api)
- [NASA's official APOD page](https://apod.nasa.gov/apod/astropix.html)

This Java service is a wrapper around the APOD API, which serves an APOD Forum Frontend, 
allowing users to retrieve paginated APODs, search for APODs, liking, commenting and saving posts. The forum is heavily inspired by [lobste.rs](https://lobste.rs/)

Check out the [APOD Forum Frontend repository](https://github.com/kkwon1/apod-forum-frontend), or 

## Personal Note

It's been in the back of my mind to build a service around this APOD API for years. I have attempted to create a very similar service back in 2019 with GoLang.
You can view it in my public repo [APODViewerService](https://github.com/kkwon1/APODViewerService).

I wanted to completely re-write the service from scratch, because:
1. I don't really remember where I was trying to go with the previous project
2. I wanted to practice writing cleaner code with stricter interfaces and a more modular approach with packages

I've been working with Java professionally for years and felt it was the easiest to pick up and just build something out.
Once the base functionality of the service is fleshed out, I may re-write this in a different language that I want to learn

## Running the Service

### Prerequisites
This application uses Maven for the build tool, so please install from [here](https://maven.apache.org/install.html)

This project is using Java 17. I personally installed it from [Amazon Corretto](https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html)

#### Windows Environment Variables
I'm developing on Windows (unfortunately), and you will need to set the appropriate environment variables to ensure these commands will work.
Namely, you will need to set JAVA_HOME and Path variables to point to the correct directories.

#### .env
The project also loads from a `.env` file, which specifies the NASA API Key, MongoDB endpoint, Auth0 token issuer, etc...
To run this project, you must set valid values for these.

### Locally
To run this service locally, run the following command from the root directory of project. The service should run on port `8082`.

```
mvn spring-boot:run
```

### Docker
To run this service on Docker, run the following commands from the root directory of project. The service should run on port `8082`.

```
> mvn package
> docker-compose up
```

Once the container is running, you can ssh into the container by using the command

```
docker exec -it <CONTAINER_NAME> sh
```

## Testing
To run the full test suite

```
mvn test
```

To run a single test file

```
mvn -DTest=<TestClassName> test
```

To run multiple specified test files

```
mvn -Dtest=<TestClassName1>,<TestClassName2> test
```

To run a single unit test from a single file

```
mvn -Dtest=<TestClassName>#<TestMethodName> test
```

## Integration Tests
To run only integration tests
```
mvn integration-test
```

To run the full suite of unit and integration tests
```
mvn verify
```

## API Guide

### Unauthenticated APIs

#### APOD