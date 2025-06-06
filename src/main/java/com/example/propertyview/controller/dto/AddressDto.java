package com.example.propertyview.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddressDto(
        @Min(1)
        @NotNull
        Integer houseNumber,
        @NotNull
        String street,
        @NotNull
        String city,
        @NotNull
        String country,
        @NotNull
        String postCode
) {
}
