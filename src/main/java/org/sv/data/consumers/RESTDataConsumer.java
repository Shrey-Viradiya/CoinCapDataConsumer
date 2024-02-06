package org.sv.data.consumers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.Callable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sv.data.dto.DataResponse;
import org.sv.data.utilities.RESTHelper;

public class RESTDataConsumer<T> implements Callable {

    private static final Logger LOGGER = LogManager.getLogger(RESTDataConsumer.class);
    private final String BASE_PATH;
    private final String host;
    private final int pollingInterval;

    public RESTDataConsumer(String host, String basePath, int pollingInterval) {
        this.BASE_PATH = basePath;
        this.host = host;
        this.pollingInterval = pollingInterval;
    }

    @Override
    public Object call() {
        String URI = this.host + BASE_PATH;

        while (true) {
            LOGGER.info("GET request to {}", URI);
            try {
                String response = RESTHelper.executeGetRequest(URI);
                DataResponse<T> dataResponse =
                        new ObjectMapper().readValue(response, new TypeReference<DataResponse<T>>() {});
                LOGGER.info(dataResponse);
                Thread.sleep(this.pollingInterval);
            } catch (Exception e) {
                LOGGER.error(e);
            }
        }
    }
}
