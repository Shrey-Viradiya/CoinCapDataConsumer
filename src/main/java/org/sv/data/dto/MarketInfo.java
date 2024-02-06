package org.sv.data.dto;

public record MarketInfo(
        String exchangeId,
        String rank,
        String baseSymbol,
        String baseId,
        String quoteSymbol,
        String quoteId,
        String priceQuote,
        String priceUsd,
        String volumeUsd24Hr,
        String percentExchangeVolume,
        String tradesCount24Hr,
        long updated) {
    // Any additional methods or customization can be added here if needed
}
