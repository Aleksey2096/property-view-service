package com.example.propertyview.service.impl;

import com.example.propertyview.repository.HotelRepository;
import com.example.propertyview.service.HistogramStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class BrandHistogramStrategy implements HistogramStrategy {

    private final HotelRepository hotelRepository;

    @Override
    public Map<String, Long> generateHistogram() {
        return fillHistogramMap(hotelRepository.countHotelsByBrand());
    }
}
