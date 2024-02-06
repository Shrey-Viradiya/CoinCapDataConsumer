package org.sv.data.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleDataHandler extends AbstractDataHandler {
    private static final Logger LOGGER = LogManager.getLogger(SimpleDataHandler.class);

    @Override
    public void handleData(String data) {
        LOGGER.info(data);
    }
}
