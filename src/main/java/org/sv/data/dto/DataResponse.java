package org.sv.data.dto;

import java.util.List;

public record DataResponse<T>(
        List<T> data,
        Long timestamp
) {
}
