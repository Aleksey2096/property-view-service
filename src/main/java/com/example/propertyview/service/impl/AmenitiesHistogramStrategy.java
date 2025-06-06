package com.example.propertyview.service.impl;

import com.example.propertyview.repository.AmenityRepository;
import com.example.propertyview.service.HistogramStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AmenitiesHistogramStrategy implements HistogramStrategy {

    private final AmenityRepository amenityRepository;

    @Override
    public Map<String, Long> generateHistogram() {
        return fillHistogramMap(amenityRepository.countHotelsByAmenity());
    }
}
