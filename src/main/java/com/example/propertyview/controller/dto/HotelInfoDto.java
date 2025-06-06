package com.example.propertyview.controller.dto;

public record HotelInfoDto(
        Long id,
        String name,
        String description,
        String address,
        String phone
) {
}
