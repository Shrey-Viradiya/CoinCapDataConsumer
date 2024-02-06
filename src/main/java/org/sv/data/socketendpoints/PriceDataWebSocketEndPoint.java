package org.sv.data.socketendpoints;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.OnMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ClientEndpoint
public class PriceDataWebSocketEndPoint {
    private static final Logger LOGGER = LogManager.getLogger(PriceDataWebSocketEndPoint.class);

    @OnMessage
    public void onMessage(String message) {
        LOGGER.info("{} received: {}", this.getClass().getSimpleName(), message);
    }
}
