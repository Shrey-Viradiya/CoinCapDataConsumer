package org.sv.data.dto;

import java.util.List;

public record ExchangesResponse(List<ExchangeInfo> data, Long timestamp) {
}
