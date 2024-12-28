FROM eclipse-temurin:23-jdk

LABEL maintainer="zakcove" 

## Build the application
# Create directory /app and change directory into /app
WORKDIR /app

# Copy files over src dest
COPY ./mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Package the application
RUN chmod a+x ./mvnw && ./mvnw package -Dmaven.test.skip=true

ENV PORT=8080
ENV SPRING_DATA_REDIS_HOST=localhost SPRING_DATA_REDIS_PORT=6379 SPRING_DATA_REDIS_DATABASE=0
ENV SPRING_DATA_REDIS_USERNAME="" SPRING_DATA_REDIS_PASSWORD=""
ENV WEATHERBIT_API_KEY="" WEATHERBIT_BASE_URL=""
ENV TICKETMASTER_API_KEY="" TICKETMASTER_BASE_URL=""

EXPOSE ${PORT}

ENTRYPOINT SERVER_PORT=${PORT} java -jar target/weatherplan-0.0.1-SNAPSHOT.jar 