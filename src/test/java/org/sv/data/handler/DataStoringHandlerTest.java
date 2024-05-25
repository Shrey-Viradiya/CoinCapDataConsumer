package org.sv.data.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.sv.data.DataStore;
import org.sv.data.dto.ExchangeInfo;

public class DataStoringHandlerTest {

    @Test
    public void testHandleData() {
        // Create an instance of DataStoringHandler
        DataStoringHandler<ExchangeInfo> handler = new DataStoringHandler<>(ExchangeInfo.class);

        // Define a test data
        String testData =
                "{\"data\": [{\"exchangeId\": \"testId\", \"name\": \"testName\", \"rank\": \"testRank\", \"percentTotalVolume\": \"testVolume\", \"volumeUsd\": \"testUsd\", \"tradingPairs\": \"testPairs\", \"socket\": true, \"exchangeUrl\": \"testUrl\", \"updated\": 1234567890}]}";

        // Call DataStoringHandler.handleData with the test data
        handler.handleData(testData);

        // Assert that the argument is as expected
        ExchangeInfo expectedExchangeInfo = new ExchangeInfo(
                "testId", "testName", "testRank", "testVolume", "testUsd", "testPairs", true, "testUrl", 1234567890L);
        assertEquals(
                List.of(expectedExchangeInfo), DataStore.getExchangeInfoList().get("testId"));
    }
}
