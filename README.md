# keycloak-auth

#Description

Simple OAuth2 authentication to manage KeyCloak tokens. It includes four APIs: login, logout, isvalid and user

# Technology

- Maven for Java dependency management
- Spring Boot 
- keycloak server
- Lombok for the models

# Functionalities
- Login, get a token given user name and password
- Logout
- IsValid, check that the keycloak token is valid
- User, get the user given the token

# How to deploy

Compile with
```
	mvn clean install
```

And execute
```
java -jar target/auth-X.X.X-SNAPSHOT.jar
```

It can also be run as:
```
    mvn spring-boot:run
```

Go to your browser and type http://localhost:8080/swagger-ui.html

Use the application properties according to your KeyCloak server configuration.

## License

Apache 2.0.

