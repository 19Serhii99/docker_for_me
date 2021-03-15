FROM maven:latest AS MAVEN_TOOL_CHAIN
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
WORKDIR /usr/src/app
RUN mvn clean package

FROM openjdk:11
EXPOSE 8080
COPY --from=MAVEN_TOOL_CHAIN /usr/src/app/target/25_docker-0.0.1-SNAPSHOT.jar /usr/app/app.jar
COPY --from=MAVEN_TOOL_CHAIN /usr/src/app/target/lib /usr/app/lib
ENTRYPOINT ["java", "-jar", "/usr/app/app.jar"]
