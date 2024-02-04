package org.sv.data.dto;

import java.util.List;

public record RatesResponse(
        List<RateInfo> data,
        Long timestamp
) {
}
