FROM anapsix/alpine-java:jdk8
ADD ${artifactId}-${version}.war app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dserver.port=8080", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
