package com.example.propertyview.model.enums;

import com.example.propertyview.exception.InvalidParameterException;

public enum HistogramParam {
    BRAND,
    CITY,
    COUNTRY,
    AMENITIES;

    public static HistogramParam fromString(String param) {
        try {
            return HistogramParam.valueOf(param.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new InvalidParameterException("Invalid parameter for histogram: " + param);
        }
    }
}
