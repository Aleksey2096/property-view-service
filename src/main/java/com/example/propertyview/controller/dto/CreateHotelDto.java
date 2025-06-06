package com.example.propertyview.controller.dto;

import jakarta.validation.constraints.NotNull;

public record CreateHotelDto(
        @NotNull
        String name,
        String description,
        @NotNull
        String brand,
        @NotNull
        AddressDto address,
        @NotNull
        ContactsDto contacts,
        @NotNull
        ArrivalTimeDto arrivalTime
) {
}
