FROM openjdk:17

COPY target/user-service-1.0-SNAPSHOT.jar /usr/local/user-service-1.0-SNAPSHOT.jar

WORKDIR /usr/local

EXPOSE 8080
CMD ["java", "-jar", "user-service-1.0-SNAPSHOT.jar"]
