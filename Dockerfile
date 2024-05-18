FROM openjdk:21-jdk
WORKDIR /app
COPY target/CoinCapDataConsumer-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
