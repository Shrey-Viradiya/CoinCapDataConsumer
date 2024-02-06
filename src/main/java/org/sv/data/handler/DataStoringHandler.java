package org.sv.data.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sv.data.DataStore;
import org.sv.data.dto.DataResponse;
import org.sv.data.dto.ExchangeInfo;

public class DataStoringHandler<T> extends AbstractDataHandler {
    private static final Logger LOGGER = LogManager.getLogger(DataStoringHandler.class);
    private final Class<T> dataClass;
    private ObjectMapper objectMapper = new ObjectMapper();

    public DataStoringHandler(Class<T> dataClass) {
        this.dataClass = dataClass;
    }

    @Override
    public void handleData(String data) {
        try {
            DataResponse dataResponse = objectMapper.readValue(data, DataResponse.class);
            LOGGER.info(dataResponse.data());
            if (dataClass.equals(ExchangeInfo.class)) {
                DataStore.updateExchangeInfoData(dataResponse.data().stream()
                        .map(dataHashSet -> objectMapper.convertValue(dataHashSet, ExchangeInfo.class))
                        .toList());
            }
        } catch (JsonProcessingException e) {
            LOGGER.error("Unable to handle data: {}", e);
        }
    }
}
