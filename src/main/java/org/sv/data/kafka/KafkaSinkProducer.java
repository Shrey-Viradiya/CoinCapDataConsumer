package org.sv.data.kafka;

import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.sv.data.config.ConfigObject;

public class KafkaSinkProducer {

    private static final Logger LOGGER = LogManager.getLogger(KafkaSinkProducer.class);
    private static KafkaProducer<String, String> kafkaProducer = null;
    private static ConfigObject applicationConfiguration = null;

    public static synchronized void initialize(ConfigObject applicationConfigurations) {
        if (Objects.isNull(kafkaProducer)) {
            Properties properties = new Properties();
            properties.setProperty(
                    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, applicationConfigurations.bootstrapServer());
            properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            kafkaProducer = new KafkaProducer<>(properties);
            applicationConfiguration = applicationConfigurations;
        }
    }

    public static synchronized void sendData(@NonNull String data) {
        if (Objects.isNull(kafkaProducer) || Objects.isNull(applicationConfiguration)) {
            throw new RuntimeException("KafkaSinkProducer not initialize.");
        }
        ProducerRecord<String, String> producerRecord =
                new ProducerRecord<>(applicationConfiguration.kafkaTopic(), data);
        Future<RecordMetadata> send = kafkaProducer.send(producerRecord);
        try {
            RecordMetadata recordMetadata = send.get();
            if (recordMetadata.hasOffset()) {
                LOGGER.info(
                        "Published to {} with offset: {}",
                        applicationConfiguration.kafkaTopic(),
                        recordMetadata.offset());
            }
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error(e);
            throw new RuntimeException(e);
        }
    }

    public static void close() {
        kafkaProducer.close();
    }
}
