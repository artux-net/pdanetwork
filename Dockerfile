FROM --platform=$TARGETPLATFORM bellsoft/liberica-openjdk-alpine:21
FROM --platform=$TARGETPLATFORM gradle:8.4.0-jdk21 as build

COPY . /home/gradle/src
WORKDIR /home/gradle/src

RUN gradle build -x test

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar", "net.artux.pdanetwork.PDANetworkApplication"]