package org.sv.data.dto;

public record ExchangeInfo(
        String exchangeId,
        String name,
        String rank,
        String percentTotalVolume,
        String volumeUsd,
        String tradingPairs,
        boolean socket,
        String exchangeUrl,
        long updated) {}
