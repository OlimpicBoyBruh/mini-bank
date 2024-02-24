FROM openjdk:17

COPY target/account-service-1.0-SNAPSHOT.jar /usr/local/account-service-1.0-SNAPSHOT.jar

WORKDIR /usr/local

EXPOSE 8190
CMD ["java", "-jar", "account-service-1.0-SNAPSHOT.jar"]
