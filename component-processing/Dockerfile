FROM adoptopenjdk/openjdk14:alpine-jre
COPY target/*.jar componentprocess.jar
ENV JAVA_OPTS=""
EXPOSE 8001
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /componentprocess.jar" ] 