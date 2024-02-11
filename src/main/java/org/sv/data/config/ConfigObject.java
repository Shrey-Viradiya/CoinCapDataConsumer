package org.sv.data.config;

public record ConfigObject(
        String host,
        Integer pollingInterval,
        String kafkaTopic,
        String bootstrapServer
) {
}
