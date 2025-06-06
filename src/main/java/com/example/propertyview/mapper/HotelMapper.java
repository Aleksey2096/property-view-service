package com.example.propertyview.mapper;

import com.example.propertyview.controller.dto.CreateHotelDto;
import com.example.propertyview.controller.dto.HotelDetailsDto;
import com.example.propertyview.controller.dto.HotelInfoDto;
import com.example.propertyview.model.entity.Address;
import com.example.propertyview.model.entity.Amenity;
import com.example.propertyview.model.entity.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface HotelMapper {

    Hotel toHotel(CreateHotelDto createHotelDto);

    @Mapping(target = "address", source = "address", qualifiedByName = "addressToString")
    @Mapping(target = "phone", source = "contacts.phone")
    HotelInfoDto toHotelInfoDto(Hotel hotel);

    @Mapping(target = "amenities", source = "amenities", qualifiedByName = "amenitiesToStrings")
    HotelDetailsDto toHotelDetailsDto(Hotel hotel);

    @Named("addressToString")
    default String mapAddressToString(Address address) {
        return String.format("%d %s, %s, %s, %s",
                address.getHouseNumber(),
                address.getStreet(),
                address.getCity(),
                address.getPostCode(),
                address.getCountry());
    }

    @Named("amenitiesToStrings")
    default List<String> mapAmenitiesToStrings(Set<Amenity> amenities) {
        return amenities.stream()
                .map(Amenity::getTitle)
                .toList();
    }
}
