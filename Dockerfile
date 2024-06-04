FROM --platform=$TARGETPLATFORM bellsoft/liberica-openjdk-alpine:latest-aarch64
ARG JAR_FILE=pdanet/build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar", "net.artux.pdanetwork.PDANetworkApplication"]