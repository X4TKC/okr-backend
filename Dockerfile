FROM openjdk:17-jdk-slim-buster
WORKDIR /app

COPY app/build/lib/* build/lib/

COPY app/build/libs/okr-backend-0.0.1.jar.jar build/

WORKDIR /app/build
ENTRYPOINT java -jar app.jar