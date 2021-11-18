FROM adoptopenjdk/openjdk11:jdk-11.0.5_10-alpine-slim
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar", "net.artux.pdanetwork.PDAMain"]