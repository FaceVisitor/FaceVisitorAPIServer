FROM openjdk:8-jdk-alpine
ARG JAR_FILE=build/libs/*.jar
ARG env
COPY ${JAR_FILE} app.jar
ENV SPRING_PROFILES_ACTIVE=${env}
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]


