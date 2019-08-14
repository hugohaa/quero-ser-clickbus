FROM maven:3.5.2-jdk-8-alpine AS MAVEN_STAGE
COPY src /usr/src/app/src  
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package

FROM openjdk:8-jdk-alpine
COPY --from=MAVEN_STAGE /usr/src/app/target/places-api-0.0.1-SNAPSHOT.jar /usr/app/places-api.jar
EXPOSE 8080
ENTRYPOINT [ "java", "-jar","/usr/app/places-api.jar" ]
