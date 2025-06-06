package com.example.propertyview.service;

import com.example.propertyview.controller.dto.CreateHotelDto;
import com.example.propertyview.controller.dto.HotelDetailsDto;
import com.example.propertyview.controller.dto.HotelInfoDto;

import java.util.List;
import java.util.Map;

public interface HotelService {

    List<HotelInfoDto> getAllHotels();

    HotelDetailsDto getHotelById(Long id);

    List<HotelInfoDto> searchHotels(String name, String brand, String city, String country, List<String> amenities);

    HotelInfoDto createHotel(CreateHotelDto createHotelDto);

    void addAmenitiesToHotel(Long hotelId, List<String> amenityTitles);

    Map<String, Long> getHotelCountGroupedByParameter(String parameter);
}
