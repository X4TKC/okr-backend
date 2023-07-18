FROM openjdk:17-jdk-slim-buster
WORKDIR /app

COPY app/build/lib/* build/lib/
CMD find . -name '*.jar'
RUN ls -al
COPY app/build/libs/okr-backend-0.0.1.jar build/

WORKDIR /app/build
ENTRYPOINT java -jar app.jar