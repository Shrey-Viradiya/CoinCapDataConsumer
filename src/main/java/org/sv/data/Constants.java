package org.sv.data;

import java.util.List;
import org.eclipse.collections.impl.list.mutable.FastList;

public class Constants {
    public static final String EXCHANGES_DATA_ENDPOINT = "/v2/exchanges";
    public static final String ASSETS_DATA_ENDPOINT = "/v2/assets";
    public static final String RATES_DATA_ENDPOINT = "/v2/rates";
    public static final String MARKETS_DATA_ENDPOINT = "/v2/markets";
    public static final String PRICES_DATA_WEBSOCKET_URL = "wss://ws.coincap.io/prices?assets=bitcoin";
    public static final String TRADES_DATA_WEBSOCKET_URL = "wss://www.nseindia.com/api/quote-equity?symbol=SBIN";
    public static final List<String> DATA_ELIGIBLE_EXCHANGES = FastList.newListWith("binance");
}
