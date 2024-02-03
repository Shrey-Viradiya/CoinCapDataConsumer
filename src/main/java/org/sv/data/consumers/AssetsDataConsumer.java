package org.sv.data.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sv.data.dto.AssetsResponse;
import org.sv.data.utilities.RESTHelper;

import java.util.concurrent.Callable;

public class AssetsDataConsumer implements Callable {
    private static final Logger LOGGER = LogManager.getLogger(AssetsDataConsumer.class);
    private static final String BASE_PATH = "/v2/assets";

    private final String host;

    public AssetsDataConsumer(String host) {
        this.host = host;
    }

    @Override
    public Object call() {
        String URI = this.host + BASE_PATH;
        LOGGER.info("GET request to {}", URI);
        try {
            String response = RESTHelper.executeGetRequest(URI);
            AssetsResponse assetsResponse = new ObjectMapper().readValue(
                    response,
                    AssetsResponse.class
            );
            LOGGER.info(assetsResponse.data());
            return response;
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return null;
    }
}
