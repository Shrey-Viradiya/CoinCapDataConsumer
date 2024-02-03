package org.sv.data.dto;

import java.util.List;

public record AssetsResponse(
        List<AssetInfo> data,
        Long timestamp) {
}
