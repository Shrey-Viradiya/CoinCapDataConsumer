package org.sv.data.socketendpoints;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.OnMessage;
import java.time.Instant;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sv.data.dto.TimeStampedCarrier;
import org.sv.data.kafka.KafkaSinkProducer;

@ClientEndpoint
public class KafkaProducingWebSocketEndPoint {

    private static final Logger LOGGER = LogManager.getLogger(KafkaProducingWebSocketEndPoint.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @OnMessage
    public synchronized void onMessage(String message) {
        LOGGER.info("Message Recieved from Web Socket: {}", message);
        try {
            Map<String, Double> resultMap = objectMapper.readValue(message, new TypeReference<>() {});
            TimeStampedCarrier timeStampedCarrier =
                    new TimeStampedCarrier(resultMap, Instant.now().toEpochMilli());
            String data = objectMapper.writeValueAsString(timeStampedCarrier);
            KafkaSinkProducer.sendData(data);
        } catch (Exception e) {
            LOGGER.error("Skipping message: {}", message);
            LOGGER.error(e);
        }
    }
}
