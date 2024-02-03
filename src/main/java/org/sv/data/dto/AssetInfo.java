package org.sv.data.dto;

public record AssetInfo(
        String id,
        String rank,
        String symbol,
        String name,
        String supply,
        String maxSupply,
        String marketCapUsd,
        String volumeUsd24Hr,
        String priceUsd,
        String changePercent24Hr,
        String vwap24Hr,
        String explorer
) {
}
