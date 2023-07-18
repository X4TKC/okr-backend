#
# Build stage
#
FROM openjdk:17-oracle AS build
COPY . .
WORKDIR /app
CMD gradlew clean
RUN ls -l /app/build/libs/
#
# Package stage
#
CMD ls -l
FROM openjdk:17-jdk-slim
COPY --from=build /target/demo-0.0.1-SNAPSHOT.jar demo.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","okr.jar"]