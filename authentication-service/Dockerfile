FROM openjdk:17
ARG JAR_FILE=target/authentication-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} authentication-service.jar
ENTRYPOINT ["java","-jar","/authentication-service.jar"]