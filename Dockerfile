FROM --platform=$TARGETPLATFORM bellsoft/liberica-openjdk-alpine:21
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar", "net.artux.pdanetwork.PDANetworkApplication"]