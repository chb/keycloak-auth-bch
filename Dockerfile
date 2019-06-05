FROM maven:3.3.9-jdk-8-alpine 

WORKDIR /code

# Prepare by downloading dependencies
ADD pom.xml /code/pom.xml
ADD src /code/src

RUN ["mvn", "clean", "install"]

EXPOSE 8081
CMD ["java", "-jar", "target/auth.jar"]