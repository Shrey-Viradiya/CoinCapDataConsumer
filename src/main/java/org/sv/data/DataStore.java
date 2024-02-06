package org.sv.data;

import java.util.List;
import org.eclipse.collections.api.multimap.list.MutableListMultimap;
import org.eclipse.collections.impl.list.mutable.ListAdapter;
import org.sv.data.dto.ExchangeInfo;

public class DataStore {
    private static MutableListMultimap<String, ExchangeInfo> exchangeInfoList;

    public static MutableListMultimap<String, ExchangeInfo> getExchangeInfoList() {
        return exchangeInfoList;
    }

    public static synchronized void updateExchangeInfoData(List<ExchangeInfo> data) {
        exchangeInfoList = ListAdapter.adapt(data).groupBy(ExchangeInfo::exchangeId);
    }
}
