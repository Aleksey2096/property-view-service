package com.example.propertyview.service;

import com.example.propertyview.model.enums.HistogramParam;
import com.example.propertyview.service.impl.AmenitiesHistogramStrategy;
import com.example.propertyview.service.impl.BrandHistogramStrategy;
import com.example.propertyview.service.impl.CityHistogramStrategy;
import com.example.propertyview.service.impl.CountryHistogramStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HistogramStrategyFactory {

    private final CityHistogramStrategy cityStrategy;
    private final CountryHistogramStrategy countryStrategy;
    private final BrandHistogramStrategy brandStrategy;
    private final AmenitiesHistogramStrategy amenitiesStrategy;

    public HistogramStrategy getStrategy(HistogramParam param) {
        return switch (param) {
            case CITY -> cityStrategy;
            case COUNTRY -> countryStrategy;
            case BRAND -> brandStrategy;
            case AMENITIES -> amenitiesStrategy;
        };
    }
}
