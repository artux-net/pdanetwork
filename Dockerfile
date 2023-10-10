FROM --platform=$TARGETPLATFORM bellsoft/liberica-openjdk-alpine:21
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-XX:+UseZGC","-Xms512m","-Xmx4096m","-jar","/app.jar", "net.artux.pdanetwork.PDANetworkApplication"]