package org.sv.data.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.sv.data.dto.ExchangesResponse;
import org.sv.data.utilities.RESTHelper;

import java.util.concurrent.Callable;

public class ExchangeDataConsumer implements Callable {
    private static final String BASE_PATH = "/v2/exchanges";

    private final String host;

    public ExchangeDataConsumer(String host) {
        this.host = host;
    }

    @Override
    public Object call() throws Exception {
        String URI = this.host + BASE_PATH;
        System.out.println("GET request to " + URI);
        String response = RESTHelper.executeGetRequest(URI);
        ExchangesResponse exchangeInfoList = new ObjectMapper().readValue(
                response,
                ExchangesResponse.class
        );
        System.out.println(exchangeInfoList.data());
        return response;
    }

}