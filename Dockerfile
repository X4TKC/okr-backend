FROM openjdk:17-jdk-slim-buster
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

RUN chmod +x ./gradlew
RUN ./gradlew bootJar

RUN cp /build/libs/okr-backend-0.0.1.jar .

EXPOSE 8080
ENTRYPOINT ["java","-jar","/okr-backend-0.0.1.jar"]