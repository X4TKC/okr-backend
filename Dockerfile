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
WORKDIR /okr-backend
COPY okr-backend/build/lib/* build/lib/
COPY okr-backend/build/libs/okr-backend-0.0.1.jar build/
WORKDIR /okr-backend/build
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","okr-backend-0.0.1.jar"]