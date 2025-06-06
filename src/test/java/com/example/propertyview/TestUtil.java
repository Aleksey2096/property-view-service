package com.example.propertyview;

import com.example.propertyview.controller.dto.AddressDto;
import com.example.propertyview.controller.dto.ArrivalTimeDto;
import com.example.propertyview.controller.dto.ContactsDto;
import com.example.propertyview.controller.dto.CreateHotelDto;
import com.example.propertyview.controller.dto.HotelDetailsDto;
import com.example.propertyview.controller.dto.HotelInfoDto;
import com.example.propertyview.exception.handler.GlobalExceptionHandler;
import com.example.propertyview.model.entity.Address;
import com.example.propertyview.model.entity.Amenity;
import com.example.propertyview.model.entity.ArrivalTime;
import com.example.propertyview.model.entity.Contacts;
import com.example.propertyview.model.entity.Hotel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@UtilityClass
public class TestUtil {
    public static final Long HOTEL_ID = RandomUtils.secure().randomLong();
    public static final String INVALID_HOTEL_ID = "invalidHotelId";
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final String TIMESTAMP_ATTRIBUTE = "timestamp";
    public static final String HOTEL_CONTROLLER_PATH = "/property-view/hotels";
    public static final String SEARCH_HOTELS_CONTROLLER_PATH = "/property-view/search";
    public static final String ADD_AMENITIES_CONTROLLER_PATH = "/property-view/hotels/%s/amenities";
    public static final String HISTOGRAM_CONTROLLER_PATH = "/property-view/histogram";
    public static final List<String> AMENITIES = List.of("Amenities 1", "Amenities 2", "Amenities 3");
    public static final String FAILED_TO_CONVERT_TO_LONG_EXCEPTION_MESSAGE = "Method parameter 'id': Failed to convert value of type 'java.lang.String' to required type 'java.lang.Long'; For input string: \"%s\"";
    public static final String MISSING_REQUIRED_ATTRIBUTE_EXCEPTION_MESSAGE = "name: must not be null";
    public static final String INVALID_HISTOGRAM_PARAMETER_EXCEPTION_MESSAGE = "Invalid parameter for histogram: ";
    public static final String HOTEL_NOT_FOUND_EXCEPTION_MESSAGE = "Hotel not found with id ";
    public static final Map<String, List<String>> HOTEL_SEARCH_PARAMS = Map.of(
            "name", List.of("DoubleTree by Hilton"),
            "brand", List.of("Hilton"),
            "city", List.of("Minsk"),
            "country", List.of("Belarus"),
            "amenities", AMENITIES
    );
    public static final CreateHotelDto INVALID_CREATE_HOTEL_DTO = new CreateHotelDto(
            null,
            null,
            HOTEL_SEARCH_PARAMS.get("brand").get(0),
            generateAddressDto(),
            generateContactsDto(),
            generateArrivalTimeDto()
    );
    public static final Map<String, Long> BRAND_HISTOGRAM = Map.of("Hilton", 3L);
    public static final String HISTOGRAM_PARAM = "brand";

    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    public static MockMvc generateMockMvc(Object controller) {
        return MockMvcBuilders
                .standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(GlobalExceptionHandler.class)
                .build();
    }

    public static HotelInfoDto generateHotelInfoDto() {
        return new HotelInfoDto(
                HOTEL_ID,
                RandomStringUtils.secure().nextAlphanumeric(10),
                RandomStringUtils.secure().nextAlphanumeric(10),
                RandomStringUtils.secure().nextAlphanumeric(10),
                RandomStringUtils.secure().next(10)
        );
    }

    public static HotelDetailsDto generateHotelDetailsDto() {
        return new HotelDetailsDto(
                HOTEL_ID,
                RandomStringUtils.secure().nextAlphanumeric(10),
                RandomStringUtils.secure().nextAlphanumeric(100),
                RandomStringUtils.secure().nextAlphanumeric(10),
                generateAddressDto(),
                generateContactsDto(),
                generateArrivalTimeDto(),
                AMENITIES
        );
    }

    public static CreateHotelDto generateCreateHotelDto() {
        return new CreateHotelDto(
                RandomStringUtils.secure().nextAlphanumeric(10),
                RandomStringUtils.secure().nextAlphanumeric(100),
                RandomStringUtils.secure().nextAlphanumeric(10),
                generateAddressDto(),
                generateContactsDto(),
                generateArrivalTimeDto()
        );
    }

    public static Hotel generateHotel() {
        return new Hotel(
                HOTEL_ID,
                RandomStringUtils.secure().nextAlphanumeric(10),
                RandomStringUtils.secure().nextAlphanumeric(100),
                RandomStringUtils.secure().nextAlphanumeric(10),
                generateAddress(),
                generateContacts(),
                generateArrivalTime(),
                new HashSet<>(Set.of(
                        new Amenity(RandomUtils.secure().randomLong(), HOTEL_SEARCH_PARAMS.get("amenities").get(0), Set.of()),
                        new Amenity(RandomUtils.secure().randomLong(), HOTEL_SEARCH_PARAMS.get("amenities").get(1), Set.of()),
                        new Amenity(RandomUtils.secure().randomLong(), HOTEL_SEARCH_PARAMS.get("amenities").get(2), Set.of())
                ))
        );
    }

    private static Address generateAddress() {
        return new Address(
                RandomUtils.secure().randomLong(),
                RandomUtils.secure().randomInt(),
                RandomStringUtils.secure().nextAlphabetic(10),
                RandomStringUtils.secure().nextAlphabetic(10),
                RandomStringUtils.secure().nextAlphabetic(10),
                RandomStringUtils.secure().nextNumeric(6)
        );
    }

    private static Contacts generateContacts() {
        return new Contacts(
                RandomUtils.secure().randomLong(),
                RandomStringUtils.secure().next(10),
                RandomStringUtils.secure().next(10)
        );
    }

    private static ArrivalTime generateArrivalTime() {
        return new ArrivalTime(
                RandomUtils.secure().randomLong(),
                LocalTime.now(),
                LocalTime.now().plusHours(RandomUtils.secure().randomInt())
        );
    }

    private static AddressDto generateAddressDto() {
        return new AddressDto(
                RandomUtils.secure().randomInt(),
                RandomStringUtils.secure().nextAlphabetic(10),
                RandomStringUtils.secure().nextAlphabetic(10),
                RandomStringUtils.secure().nextAlphabetic(10),
                RandomStringUtils.secure().nextNumeric(6)
        );
    }

    private static ContactsDto generateContactsDto() {
        return new ContactsDto(
                RandomStringUtils.secure().next(10),
                RandomStringUtils.secure().next(10)
        );
    }

    private static ArrivalTimeDto generateArrivalTimeDto() {
        return new ArrivalTimeDto(
                LocalTime.now(),
                LocalTime.now().plusHours(RandomUtils.secure().randomInt())
        );
    }
}
