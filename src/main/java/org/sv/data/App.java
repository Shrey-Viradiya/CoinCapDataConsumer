package org.sv.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
import org.sv.data.socketendpoints.PriceDataWebSocketEndPoint;
import org.sv.data.socketendpoints.TradesDataWebSocketEndpoint;

public class App {

    private static final Logger LOGGER = LogManager.getLogger(App.class);

    public static void main(String[] args) throws IOException {
        ConfigObject applicationConfiguration = readConfigFromResource("configuration.yaml");
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        LOGGER.info("Starting with the no of processors: {}", availableProcessors);

        ExecutorService executor = Executors.newFixedThreadPool(availableProcessors);

        try {
            executor.invokeAll(getInitilizationCallables(applicationConfiguration));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Collection callables = FastList.newList();
        callables.addAll(getRESTDataConsumerCallables(applicationConfiguration));
        callables.addAll(getWebSocketConsumerCallables(applicationConfiguration));

        try {
            executor.invokeAll(callables);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        executor.shutdownNow();
    }

    public static ConfigObject readConfigFromResource(String resourceName) throws IOException {
        try (InputStream inputStream = App.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (inputStream == null) {
                throw new IOException("Resource not found");
            }

            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            return objectMapper.readValue(inputStream, ConfigObject.class);
        }
    }

    private static Collection getInitilizationCallables(ConfigObject applicationConfiguration) {
        Collection callables = new ArrayList();
        callables.add(new RESTDataConsumer<ExchangeInfo>(
                applicationConfiguration.host(),
                Constants.EXCHANGES_DATA_ENDPOINT,
                applicationConfiguration.pollingInterval(),
                new DataStoringHandler<>(ExchangeInfo.class),
                1));
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

    private static Collection getWebSocketConsumerCallables(ConfigObject applicationConfiguration) {
        try {
            Thread.sleep(applicationConfiguration.pollingInterval());
        } catch (InterruptedException e) {
            LOGGER.error("Thread Sleep Failed", e);
        }

        Collection callables = new ArrayList();
        callables.add(
                new WebSocketDataConsumer<>(Constants.PRICES_DATA_WEBSOCKET_URL, PriceDataWebSocketEndPoint.class));

        DataStore.getExchangeInfoList()
                .keysView()
                .forEach(exchange -> callables.add(new WebSocketDataConsumer<>(
                        Constants.TRADES_DATA_WEBSOCKET_URL.replace("EXCHANGE", exchange),
                        TradesDataWebSocketEndpoint.class)));

        return callables;
    }
}
