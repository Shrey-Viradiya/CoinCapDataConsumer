package org.sv.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.eclipse.collections.api.multimap.list.MutableListMultimap;
import org.junit.jupiter.api.Test;
import org.sv.data.dto.ExchangeInfo;

class DataStoreTest {

    @Test
    void testUpdateExchangeInfoData() {
        // Create a list of ExchangeInfo objects
        List<ExchangeInfo> data = Arrays.asList(
                new ExchangeInfo(
                        "exchange1",
                        "name1",
                        "rank1",
                        "percentTotalVolume1",
                        "volumeUsd1",
                        "tradingPairs1",
                        true,
                        "exchangeUrl1",
                        123456789L),
                new ExchangeInfo(
                        "exchange2",
                        "name2",
                        "rank2",
                        "percentTotalVolume2",
                        "volumeUsd2",
                        "tradingPairs2",
                        true,
                        "exchangeUrl2",
                        123459893L));

        // Update the DataStore
        DataStore.updateExchangeInfoData(data);

        // Retrieve the updated list
        MutableListMultimap<String, ExchangeInfo> updatedList = DataStore.getExchangeInfoList();

        // Assert that the updated list is equal to the original list
        assertEquals(data.size(), updatedList.size());
    }
}
