package org.sv.data.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.sv.data.dto.AssetsResponse;
import org.sv.data.utilities.RESTHelper;

import java.util.concurrent.Callable;

public class AssetsDataConsumer implements Callable {
    private static final String BASE_PATH = "/v2/assets";

    private final String host;

    public AssetsDataConsumer(String host) {
        this.host = host;
    }

    @Override
    public Object call() throws Exception {
        String URI = this.host + BASE_PATH;
        System.out.println("GET request to " + URI);
        try {
            String response = RESTHelper.executeGetRequest(URI);
            AssetsResponse assetsResponse = new ObjectMapper().readValue(
                    response,
                    AssetsResponse.class
            );
            System.out.println(assetsResponse.data());
        } catch (Exception e) {

        }
        return null;
    }
}
