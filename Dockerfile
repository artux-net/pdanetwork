FROM --platform=$TARGETPLATFORM openjdk:19
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar", "net.artux.pdanetwork.PDANetworkApplication"]