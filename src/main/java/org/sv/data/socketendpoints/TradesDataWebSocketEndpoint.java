package org.sv.data.socketendpoints;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.OnMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ClientEndpoint
public class TradesDataWebSocketEndpoint {
    private static final Logger LOGGER = LogManager.getLogger(TradesDataWebSocketEndpoint.class);

    @OnMessage
    public synchronized void onMessage(String message) {
        LOGGER.info("{} received: {}", this.getClass().getSimpleName(), message);
    }
}
