package org.sv.data.socketendpoints;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.OnMessage;
import org.sv.data.kafka.KafkaSinkProducer;

@ClientEndpoint
public class KafkaProducingWebSocketEndPoint {

    @OnMessage
    public synchronized void onMessage(String message) {
        KafkaSinkProducer.sendData(message);
    }
}
