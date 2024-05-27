# CoinCapDataConsumer

This project consumes data from the CoinCap public APIs and puts them into a sink.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing
purposes.

### Prerequisites

What things you need to install the software and how to install them:

- Java 21
- Maven
- Docker (for running Kafka)

Use the following commands to setup the kafka container in your local docker.

```bash
docker run -d --name zookeeper -e ALLOW_ANONYMOUS_LOGIN=yes bitnami/zookeeper:latest\ndocker run -d --name kafka -e KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper:2181 -e ALLOW_PLAINTEXT_LISTENER=yes -p 9092:9092 --link zookeeper:zookeeper bitnami/kafka:latest

docker network create app-tier --driver bridge

docker run -d --name kafka-server --hostname kafka-server 
    --network app-tier 
    -e KAFKA_CFG_NODE_ID=0 
    -e KAFKA_CFG_PROCESS_ROLES=controller,broker 
    -e KAFKA_CFG_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093 
    -e KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT 
    -e KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=0@kafka-server:9093 
    -e KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER 
    bitnami/kafka:latest
```

## Setup

To run this project, you need to have Java, Maven, and Kafka installed on your machine.

1. Clone the repository:

```bash
git clone https://github.com/Shrey-Viradiya/CoinCapDataConsumer.git
```

2. Navigate to the project directory:

```bash
cd CoinCapDataConsumer
```

3. Build the project:

```bash
mvn clean install
```

4. Run the project:

```bash
java -cp target/CoinCapDataConsumer-1.0-SNAPSHOT.jar <<path/to/configuration_file.yaml>> 
```

or

use can use docker image.

```bash
docker build -t coin-cap-consumer .
docker run --name BRTPP -d coin-cap-consumer
```

## Built With

- [Java](https://www.java.com/) - The programming language used
- [Maven](https://maven.apache.org/) - Dependency Management
- [Kafka](https://kafka.apache.org/) - Used for real-time data streaming