FROM adoptopenjdk/openjdk14:alpine-jre
COPY target/*.jar authentication.jar
ENV JAVA_OPTS=""
EXPOSE 8000
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /authentication.jar" ] 