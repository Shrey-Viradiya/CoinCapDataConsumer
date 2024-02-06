package org.sv.data.consumers;

import java.util.concurrent.Callable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sv.data.handler.AbstractDataHandler;
import org.sv.data.utilities.RESTHelper;

public class RESTDataConsumer<T> implements Callable {

    private static final Logger LOGGER = LogManager.getLogger(RESTDataConsumer.class);
    private final String BASE_PATH;
    private final String host;
    private final int pollingInterval;
    private final AbstractDataHandler dataHandler;

    public RESTDataConsumer(String host, String basePath, int pollingInterval, AbstractDataHandler dataHandler) {
        this.BASE_PATH = basePath;
        this.host = host;
        this.pollingInterval = pollingInterval;
        this.dataHandler = dataHandler;
    }

    @Override
    public Object call() {
        String URI = this.host + BASE_PATH;

        while (true) {
            LOGGER.info("GET request to {}", URI);
            try {
                String response = RESTHelper.executeGetRequest(URI);
                dataHandler.handleData(response);
                Thread.sleep(this.pollingInterval);
            } catch (Exception e) {
                LOGGER.error(e);
            }
        }
    }
}
