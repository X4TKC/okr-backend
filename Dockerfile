#
# Build stage
#
FROM openjdk:17-oracle AS build
COPY . .
#RUN mvn clean package -DskipTests

#
# Package stage
#
CMD ls -l
FROM openjdk:17-jdk-slim
COPY --from=build /target/okr-backend-0.0.1.jar okr.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","okr.jar"]