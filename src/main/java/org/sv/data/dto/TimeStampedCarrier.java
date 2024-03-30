package org.sv.data.dto;

import java.util.Map;

public record TimeStampedCarrier(Map<String, Double> data, Long timestamp) {}
