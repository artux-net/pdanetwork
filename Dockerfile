FROM --platform=$TARGETPLATFORM bellsoft/liberica-openjdk-alpine:latest-aarch64
ARG JAR_FILE=pdanet/build/libs/*.jar
COPY ${JAR_FILE} app.jar

HEALTHCHECK --interval=15s --timeout=5s --start-period=45s --retries=5 \
    CMD wget --spider --quiet http://localhost:8080/pdanetwork/actuator/health || exit 1

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar", "net.artux.pdanetwork.PDANetworkApplication"]