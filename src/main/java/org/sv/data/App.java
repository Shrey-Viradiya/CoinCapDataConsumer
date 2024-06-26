package org.sv.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.sv.data.config.ConfigObject;
import org.sv.data.consumers.RESTDataConsumer;
import org.sv.data.consumers.WebSocketDataConsumer;
import org.sv.data.dto.AssetInfo;
import org.sv.data.dto.ExchangeInfo;
import org.sv.data.dto.MarketInfo;
import org.sv.data.dto.RateInfo;
import org.sv.data.handler.DataStoringHandler;
import org.sv.data.handler.SimpleDataHandler;
import org.sv.data.kafka.KafkaSinkProducer;
import org.sv.data.socketendpoints.KafkaProducingWebSocketEndPoint;

public class App {

    private static final Logger LOGGER = LogManager.getLogger(App.class);

    public static void main(String[] args) throws IOException {
        LOGGER.info("Starting Coin Cap Consumer Application");
        ConfigObject applicationConfiguration = readConfigFromFile(args[0]);
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        KafkaSinkProducer.initialize(applicationConfiguration);
        LOGGER.info("Starting with the no of processors: {}", availableProcessors);

        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("CoinCapDataConsumer-thread-%d")
                .build();

        ExecutorService executor = Executors.newFixedThreadPool(availableProcessors, threadFactory);

        Collection callables = FastList.newList();
        //        callables.addAll(getRESTDataConsumerCallables(applicationConfiguration));
        callables.addAll(getWebSocketConsumerCallables(applicationConfiguration));

        try {
            executor.invokeAll(callables);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            executor.shutdownNow();
            KafkaSinkProducer.close();
        }
    }

    public static ConfigObject readConfigFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("File not found at " + filePath);
        }

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            return objectMapper.readValue(fileInputStream, ConfigObject.class);
        } catch (Exception e) {
            LOGGER.error(e);
            throw e;
        }
    }

    private static Collection getWebSocketConsumerCallables(ConfigObject applicationConfiguration) {
        Collection callables = new ArrayList();
        callables.add(new WebSocketDataConsumer<>(
                Constants.PRICES_DATA_WEBSOCKET_URL, KafkaProducingWebSocketEndPoint.class));

        //        Constants.DATA_ELIGIBLE_EXCHANGES.forEach(exchange -> callables.add(new WebSocketDataConsumer<>(
        //                Constants.TRADES_DATA_WEBSOCKET_URL.replace("EXCHANGE", exchange),
        // SimpleDataWebSocketEndPoint.class)));

        return callables;
    }

    private static Collection getRESTDataConsumerCallables(ConfigObject applicationConfiguration) {
        Collection callables = new ArrayList();
        callables.add(new RESTDataConsumer<ExchangeInfo>(
                applicationConfiguration.host(),
                Constants.EXCHANGES_DATA_ENDPOINT,
                applicationConfiguration.pollingInterval(),
                new DataStoringHandler<>(ExchangeInfo.class)));

        callables.add(new RESTDataConsumer<AssetInfo>(
                applicationConfiguration.host(),
                Constants.ASSETS_DATA_ENDPOINT,
                applicationConfiguration.pollingInterval(),
                new SimpleDataHandler()));

        callables.add(new RESTDataConsumer<RateInfo>(
                applicationConfiguration.host(),
                Constants.RATES_DATA_ENDPOINT,
                applicationConfiguration.pollingInterval(),
                new SimpleDataHandler()));

        callables.add(new RESTDataConsumer<MarketInfo>(
                applicationConfiguration.host(),
                Constants.MARKETS_DATA_ENDPOINT,
                applicationConfiguration.pollingInterval(),
                new SimpleDataHandler()));
        return callables;
    }
}
