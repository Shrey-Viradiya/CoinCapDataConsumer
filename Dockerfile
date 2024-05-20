FROM openjdk:21-jdk
WORKDIR /app
COPY target/CoinCapDataConsumer-1.0-SNAPSHOT.jar app.jar
COPY src/main/resources/configuration.yaml configuration.yaml
COPY src/main/resources/log4j2.xml log4j2.xml
ENTRYPOINT ["java", "-Dlog4j2.configurationFile=log4j2.xml", "-jar", "app.jar", "configuration.yaml"]
