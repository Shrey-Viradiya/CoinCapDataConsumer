package org.sv.data.socketendpoints;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.OnMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sv.data.kafka.KafkaSinkProducer;

@ClientEndpoint
public class KafkaProducingWebSocketEndPoint {

    private static final Logger LOGGER = LogManager.getLogger(KafkaProducingWebSocketEndPoint.class);

    @OnMessage
    public synchronized void onMessage(String message) {
        LOGGER.info("Message Recieved from Web Socket: {}", message);
        KafkaSinkProducer.sendData(message);
    }
}
