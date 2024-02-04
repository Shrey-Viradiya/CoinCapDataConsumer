package org.sv.data.consumers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sv.data.utilities.RESTHelper;

import java.util.concurrent.Callable;

public class DataConsumer<T> implements Callable {

    private static final Logger LOGGER = LogManager.getLogger(DataConsumer.class);
    private final String BASE_PATH;
    private final String host;

    public DataConsumer(String host, String basePath) {
        this.BASE_PATH = basePath;
        this.host = host;
    }

    @Override
    public Object call() {
        String URI = this.host + BASE_PATH;
        LOGGER.info("GET request to {}", URI);
        try {
            String response = RESTHelper.executeGetRequest(URI);
            T dataResponse = new ObjectMapper().readValue(
                    response,
                    new TypeReference<T>() {
                    }
            );
            LOGGER.info(dataResponse);
            return response;
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return null;
    }
}
