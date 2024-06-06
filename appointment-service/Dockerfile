FROM openjdk:17
ARG JAR_FILE=target/appointment-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} appointment-service.jar
ENTRYPOINT ["java","-jar","/appointment-service.jar"]