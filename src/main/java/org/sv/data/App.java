package org.sv.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sv.data.config.ConfigObject;
import org.sv.data.consumers.DataConsumer;
import org.sv.data.dto.AssetsResponse;
import org.sv.data.dto.ExchangesResponse;
import org.sv.data.dto.RatesResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {

    private static final Logger LOGGER = LogManager.getLogger(App.class);

    public static void main(String[] args) throws IOException {
        ConfigObject applicationConfiguration = readConfigFromResource("configuration.yaml");
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        LOGGER.info("Starting with the no of processors: {}", availableProcessors);

        ExecutorService executor = Executors.newFixedThreadPool(availableProcessors);

        Collection callables = new ArrayList();
        callables.add(new DataConsumer<ExchangesResponse>(applicationConfiguration.host(), "/v2/exchanges"));
        callables.add(new DataConsumer<AssetsResponse>(applicationConfiguration.host(), "/v2/assets"));
        callables.add(new DataConsumer<RatesResponse>(applicationConfiguration.host(), "/v2/rates"));
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
