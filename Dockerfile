FROM openjdk:21-jdk
WORKDIR /app
COPY target/CoinCapDataConsumer-1.0-SNAPSHOT.jar app.jar
COPY src/main/resources/configuration.yaml configuration.yaml
ENTRYPOINT ["java", "-jar", "app.jar", "configuration.yaml"]
