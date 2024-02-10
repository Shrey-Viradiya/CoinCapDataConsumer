package org.sv.data.handler;

import org.sv.data.kafka.KafkaSinkProducer;

public class KafkaSinkProducerHandler extends AbstractDataHandler {

    @Override
    public void handleData(String data) {
        KafkaSinkProducer.sendData(data);
    }
}
