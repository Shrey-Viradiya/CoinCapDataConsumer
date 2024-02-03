package org.sv.data.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sv.data.dto.ExchangesResponse;
import org.sv.data.utilities.RESTHelper;

import java.util.concurrent.Callable;

public class ExchangeDataConsumer implements Callable {
    private static final Logger LOGGER = LogManager.getLogger(ExchangeDataConsumer.class);
    private static final String BASE_PATH = "/v2/exchanges";

    private final String host;

    public ExchangeDataConsumer(String host) {
        this.host = host;
    }

    @Override
    public Object call() {
        String URI = this.host + BASE_PATH;
        LOGGER.info("GET request to {}", URI);
        try {
            String response = RESTHelper.executeGetRequest(URI);
            ExchangesResponse exchangeInfoList = new ObjectMapper().readValue(
                    response,
                    ExchangesResponse.class
            );
            LOGGER.info(exchangeInfoList.data());
            return response;
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return null;
    }

}