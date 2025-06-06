package com.example.propertyview.mapper;

import com.example.propertyview.controller.dto.CreateHotelDto;
import com.example.propertyview.controller.dto.HotelDetailsDto;
import com.example.propertyview.controller.dto.HotelInfoDto;
import com.example.propertyview.model.entity.Hotel;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import static com.example.propertyview.TestUtil.generateCreateHotelDto;
import static com.example.propertyview.TestUtil.generateHotel;
import static org.assertj.core.api.Assertions.assertThat;

class HotelMapperTest {
    private final HotelMapper hotelMapper = new HotelMapperImpl();

    private final CreateHotelDto createHotelDto = generateCreateHotelDto();
    private final Hotel hotel = generateHotel();

    @Test
    void toHotel_whenDataIsValid_shouldReturnHotel() {
        Hotel result = hotelMapper.toHotel(createHotelDto);

        SoftAssertions.assertSoftly(softAssertions -> {
            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo(createHotelDto.name());
            assertThat(result.getDescription()).isEqualTo(createHotelDto.description());
            assertThat(result.getBrand()).isEqualTo(createHotelDto.brand());
            assertThat(result.getAddress().getHouseNumber()).isEqualTo(createHotelDto.address().houseNumber());
            assertThat(result.getAddress().getStreet()).isEqualTo(createHotelDto.address().street());
            assertThat(result.getAddress().getCity()).isEqualTo(createHotelDto.address().city());
            assertThat(result.getAddress().getCountry()).isEqualTo(createHotelDto.address().country());
            assertThat(result.getAddress().getPostCode()).isEqualTo(createHotelDto.address().postCode());
            assertThat(result.getContacts().getPhone()).isEqualTo(createHotelDto.contacts().phone());
            assertThat(result.getContacts().getEmail()).isEqualTo(createHotelDto.contacts().email());
            assertThat(result.getArrivalTime().getCheckIn()).isEqualTo(createHotelDto.arrivalTime().checkIn());
            assertThat(result.getArrivalTime().getCheckOut()).isEqualTo(createHotelDto.arrivalTime().checkOut());
        });
    }

    @Test
    void toHotelInfoDto_whenDataIsValid_shouldReturnHotelInfoDto() {
        HotelInfoDto result = hotelMapper.toHotelInfoDto(hotel);

        SoftAssertions.assertSoftly(softAssertions -> {
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(hotel.getId());
            assertThat(result.name()).isEqualTo(hotel.getName());
            assertThat(result.description()).isEqualTo(hotel.getDescription());
            assertThat(result.phone()).isEqualTo(hotel.getContacts().getPhone());
        });
    }

    @Test
    void toHotelDetailsDto_whenDataIsValid_shouldReturnHotelDetailsDto() {
        HotelDetailsDto result = hotelMapper.toHotelDetailsDto(hotel);

        SoftAssertions.assertSoftly(softAssertions -> {
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(hotel.getId());
            assertThat(result.name()).isEqualTo(hotel.getName());
            assertThat(result.brand()).isEqualTo(hotel.getBrand());
            assertThat(result.address().houseNumber()).isEqualTo(hotel.getAddress().getHouseNumber());
            assertThat(result.address().street()).isEqualTo(hotel.getAddress().getStreet());
            assertThat(result.address().city()).isEqualTo(hotel.getAddress().getCity());
            assertThat(result.address().country()).isEqualTo(hotel.getAddress().getCountry());
            assertThat(result.address().postCode()).isEqualTo(hotel.getAddress().getPostCode());
            assertThat(result.contacts().email()).isEqualTo(hotel.getContacts().getEmail());
            assertThat(result.contacts().phone()).isEqualTo(hotel.getContacts().getPhone());
            assertThat(result.arrivalTime().checkIn()).isEqualTo(hotel.getArrivalTime().getCheckIn());
            assertThat(result.arrivalTime().checkOut()).isEqualTo(hotel.getArrivalTime().getCheckOut());
        });
    }
}
