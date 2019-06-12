# keycloak-auth

## Description

Simple OAuth2 authentication to manage KeyCloak tokens. It includes four APIs: login and user

## Technology

- Maven for Java dependency management
- Spring Boot 
- keycloak client
- Lombok for the models

## Functionalities

- Login, get a token given user name and password
- User, get the user given the token

## How to deploy

Compile with

```
mvn clean install
```

And execute

```
java -jar target/auth.jar
```

It can also be run as:

```
mvn spring-boot:run
```

Go to your browser and type http://localhost:8081/swagger-ui.html

Use the application properties according to your KeyCloak server configuration.

## KeyCloak configuration

- Create a realm for your project
- Create a client in the realm
	- Valid Redirect URIs: Put "http://localhost:9090/*"
- Create a valid role and a user changing the password

## Environment variables

    KEYCLOAK_URL=
    KEYCLOAK_REALM=
    KEYCLOAK_CLIENT_ID=

## Docker deployment

The KeyCloak parameters are configured using environment variables inside the `application.yml` file:

```
docker run --name auth -d -e KEYCLOAK_URL=http://localhost:9090/auth -e KEYCLOAK_REALM=test -e KEYCLOAK_CLIENT_ID=test health/auth
```

Logging can be also configured using `LOGGING_FOLDER` and sharing a volume (for [ELK](https://www.elastic.co/elk-stack) processing). The level of the logging can be configured with `LOGGING_MODE` (dev|prod):

```
docker run --name auth -d -v /home/docker/log/test:/log/test -e KEYCLOAK_URL=http://localhost:9090/auth -e KEYCLOAK_REALM=test -e KEYCLOAK_CLIENT_ID=test -e LOGGING_FOLDER=/log/test -e LOGGING_MODE=dev health/auth
```

## License

Apache 2.0.

