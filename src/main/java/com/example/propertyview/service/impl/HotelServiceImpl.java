package com.example.propertyview.service.impl;

import com.example.propertyview.controller.dto.CreateHotelDto;
import com.example.propertyview.controller.dto.HotelDetailsDto;
import com.example.propertyview.controller.dto.HotelInfoDto;
import com.example.propertyview.exception.ResourceNotFoundException;
import com.example.propertyview.mapper.HotelMapper;
import com.example.propertyview.model.entity.Amenity;
import com.example.propertyview.model.entity.Hotel;
import com.example.propertyview.model.enums.HistogramParam;
import com.example.propertyview.repository.AmenityRepository;
import com.example.propertyview.repository.HotelRepository;
import com.example.propertyview.service.HistogramStrategyFactory;
import com.example.propertyview.service.HotelService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final AmenityRepository amenityRepository;
    private final HotelMapper hotelMapper;
    private final HistogramStrategyFactory histogramFactory;

    @Override
    public List<HotelInfoDto> getAllHotels() {
        return hotelRepository
                .findAll()
                .stream()
                .map(hotelMapper::toHotelInfoDto)
                .toList();
    }

    @Override
    public HotelDetailsDto getHotelById(Long id) {
        return hotelMapper.toHotelDetailsDto(findHotelById(id));
    }

    @Override
    public List<HotelInfoDto> searchHotels(String name, String brand, String city, String country, List<String> amenities) {
        List<Hotel> filteredByFields = hotelRepository.searchByParams(name, brand, city, country);
        if (CollectionUtils.isEmpty(amenities)) {
            return filteredByFields.stream().map(hotelMapper::toHotelInfoDto).toList();
        }
        List<Hotel> matched = filteredByFields.stream()
                .filter(hotel -> hotel.getAmenities().stream()
                        .map(a -> a.getTitle().toLowerCase())
                        .collect(Collectors.toSet())
                        .containsAll(amenities.stream().map(String::toLowerCase).toList()))
                .toList();
        return matched.stream().map(hotelMapper::toHotelInfoDto).toList();
    }

    @Override
    @Transactional
    public HotelInfoDto createHotel(CreateHotelDto createHotelDto) {
        Hotel hotel = hotelRepository.save(hotelMapper.toHotel(createHotelDto));
        return hotelMapper.toHotelInfoDto(hotel);
    }

    @Override
    @Transactional
    public void addAmenitiesToHotel(Long hotelId, List<String> amenityTitles) {
        Hotel hotel = findHotelById(hotelId);
        for (String title : amenityTitles) {
            Amenity amenity = amenityRepository
                    .findByTitle(title)
                    .orElseGet(() -> new Amenity(title));
            hotel.getAmenities().add(amenity);
        }
    }

    @Override
    public Map<String, Long> getHotelCountGroupedByParameter(String parameter) {
        return histogramFactory
                .getStrategy(HistogramParam.fromString(parameter))
                .generateHistogram();
    }

    private Hotel findHotelById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id " + id));
    }
}
