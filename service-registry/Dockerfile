FROM adoptopenjdk/openjdk14:alpine-jre
COPY target/*.jar service-registry.jar
ENV JAVA_OPTS=""
EXPOSE 8761
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /service-registry.jar" ] 