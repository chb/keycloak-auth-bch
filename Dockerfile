# Copyright (C) 2018  Atos Spain SA. All rights reserved.
# 
# This file is part of the KeyCloak Auth API.
# 
# This file is free software: you can redistribute it and/or modify it under the 
# terms of the Apache License, Version 2.0 (the License);
# 
# http://www.apache.org/licenses/LICENSE-2.0
# 
# The software is provided "AS IS", without any warranty of any kind, express or implied,
# including but not limited to the warranties of merchantability, fitness for a particular
# purpose and noninfringement, in no event shall the authors or copyright holders be 
# liable for any claim, damages or other liability, whether in action of contract, tort or
# otherwise, arising from, out of or in connection with the software or the use or other
# dealings in the software.
# 
# See README file for the full disclaimer information and LICENSE file for full license 
# information in the project root.

FROM maven:3.8-eclipse-temurin-11 AS builder
WORKDIR /code
COPY pom.xml .
RUN mvn --debug dependency:resolve
COPY src ./src
RUN ["mvn", "package", "-DskipTests=true"]

FROM openjdk:11
COPY --from=builder /code/target/auth.war /
EXPOSE 8081
CMD ["java", "-jar", "./auth.war"]