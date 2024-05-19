FROM --platform=$TARGETPLATFORM bellsoft/liberica-openjdk-alpine:21
ARG JAR_FILE=pdanet/build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar", "net.artux.pdanetwork.PDANetworkApplication"]