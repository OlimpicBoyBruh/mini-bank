FROM arm64v8/openjdk:17

COPY target/operation-service-1.0-SNAPSHOT.jar /usr/local/operation-service-1.0-SNAPSHOT.jar

WORKDIR /usr/local

EXPOSE 8085
CMD ["java", "-jar", "operation-service-1.0-SNAPSHOT.jar"]
