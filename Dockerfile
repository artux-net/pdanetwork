FROM adoptopenjdk/openjdk11:jdk-11.0.5_10-alpine-slim
RUN apk add fontconfig ttf-dejavu
VOLUME /data/pdanetwork
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar","net.artux.pdanetwork.PDAMain"]