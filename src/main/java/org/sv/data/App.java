package org.sv.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.sv.data.config.ConfigObject;
import org.sv.data.consumers.AssetsDataConsumer;
import org.sv.data.consumers.ExchangeDataConsumer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {
    public static void main(String[] args) throws IOException {
        ConfigObject applicationConfiguration = readConfigFromResource("configuration.yaml");
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        System.out.println("Starting with the no of processors: " + availableProcessors);
        ExecutorService executor = Executors.newFixedThreadPool(availableProcessors);
        Collection callables = new ArrayList();
        callables.add(new ExchangeDataConsumer(applicationConfiguration.host()));
        callables.add(new AssetsDataConsumer(applicationConfiguration.host()));
        try {
            executor.invokeAll(callables);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        executor.shutdownNow();
    }

    public static ConfigObject readConfigFromResource(String resourceName) throws IOException {
        try (InputStream inputStream = App.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (inputStream == null) {
                throw new IOException("Resource not found");
            }

            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            return objectMapper.readValue(inputStream, ConfigObject.class);
        }
    }
}
