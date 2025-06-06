package com.example.propertyview.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record ContactsDto(
        @NotNull
        String phone,
        @Email
        @NotNull
        String email
) {
}
