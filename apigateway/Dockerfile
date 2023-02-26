FROM adoptopenjdk/openjdk14:alpine-jre
COPY target/*.jar api-gateway.jar
ENV JAVA_OPTS=""
EXPOSE 8080
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /api-gateway.jar" ] 