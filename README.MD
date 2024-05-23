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