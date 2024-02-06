package org.sv.data.consumers;

import jakarta.websocket.ContainerProvider;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import java.net.URI;
import java.util.concurrent.Callable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WebSocketDataConsumer<T> implements Callable {
    private static final Logger LOGGER = LogManager.getLogger(WebSocketDataConsumer.class);

    private final String url;
    private final Class<T> endPointClass;

    public WebSocketDataConsumer(String url, Class<T> endPointClass) {
        this.url = url;
        this.endPointClass = endPointClass;
    }

    @Override
    public Object call() throws Exception {
        LOGGER.info("Starting the thread for {}", endPointClass.getSimpleName());
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try (Session session = container.connectToServer(endPointClass, URI.create(this.url))) {
            while (true) {}
        } catch (DeploymentException e) {
            throw new RuntimeException(e);
        }
    }
}
