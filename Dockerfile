FROM --platform=$TARGETPLATFORM bellsoft/liberica-openjdk-alpine:21
FROM --platform=$TARGETPLATFORM gradle:8.4.0-jdk21-alpine
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon -x test

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar", "net.artux.pdanetwork.PDANetworkApplication"]