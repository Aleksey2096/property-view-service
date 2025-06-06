package com.example.propertyview.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface HistogramStrategy {

    Map<String, Long> generateHistogram();

    default Map<String, Long> fillHistogramMap(List<Object[]> rows) {
        return rows.stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1]
                ));
    }
}
