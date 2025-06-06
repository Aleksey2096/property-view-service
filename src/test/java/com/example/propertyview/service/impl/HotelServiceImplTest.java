package com.example.propertyview.service.impl;

import com.example.propertyview.controller.dto.CreateHotelDto;
import com.example.propertyview.controller.dto.HotelDetailsDto;
import com.example.propertyview.controller.dto.HotelInfoDto;
import com.example.propertyview.exception.InvalidParameterException;
import com.example.propertyview.exception.ResourceNotFoundException;
import com.example.propertyview.mapper.HotelMapper;
import com.example.propertyview.model.entity.Hotel;
import com.example.propertyview.model.enums.HistogramParam;
import com.example.propertyview.repository.AmenityRepository;
import com.example.propertyview.repository.HotelRepository;
import com.example.propertyview.service.HistogramStrategyFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.propertyview.TestUtil.BRAND_HISTOGRAM;
import static com.example.propertyview.TestUtil.HISTOGRAM_PARAM;
import static com.example.propertyview.TestUtil.HOTEL_ID;
import static com.example.propertyview.TestUtil.HOTEL_NOT_FOUND_EXCEPTION_MESSAGE;
import static com.example.propertyview.TestUtil.HOTEL_SEARCH_PARAMS;
import static com.example.propertyview.TestUtil.AMENITIES;
import static com.example.propertyview.TestUtil.INVALID_HISTOGRAM_PARAMETER_EXCEPTION_MESSAGE;
import static com.example.propertyview.TestUtil.INVALID_HOTEL_ID;
import static com.example.propertyview.TestUtil.generateCreateHotelDto;
import static com.example.propertyview.TestUtil.generateHotel;
import static com.example.propertyview.TestUtil.generateHotelDetailsDto;
import static com.example.propertyview.TestUtil.generateHotelInfoDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {
    @InjectMocks
    private HotelServiceImpl hotelService;

    @Mock
    private HotelRepository hotelRepository;
    @Mock
    private AmenityRepository amenityRepository;
    @Mock
    private HotelMapper hotelMapper;
    @Mock
    private HistogramStrategyFactory histogramFactory;
    @Mock
    private BrandHistogramStrategy brandHistogramStrategy;

    private final CreateHotelDto createHotelDto = generateCreateHotelDto();
    private final HotelInfoDto hotelInfoDto = generateHotelInfoDto();
    private final List<HotelInfoDto> hotelInfoDtoList = List.of(hotelInfoDto);
    private final HotelDetailsDto hotelDetailsDto = generateHotelDetailsDto();
    private final Hotel hotel = generateHotel();
    private final List<Hotel> hotelList = List.of(hotel);

    @Test
    void getAllHotels_whenHotelsExist_shouldReturnAllHotels() {
        when(hotelRepository.findAll()).thenReturn(hotelList);
        when(hotelMapper.toHotelInfoDto(hotel)).thenReturn(hotelInfoDto);

        List<HotelInfoDto> actualHotels = hotelService.getAllHotels();

        assertThat(actualHotels).isEqualTo(hotelInfoDtoList);

        verify(hotelRepository, times(1)).findAll();
        verify(hotelMapper, times(1)).toHotelInfoDto(hotel);
    }

    @Test
    void getAllHotels_whenHotelsDoNotExist_shouldReturnEmptyList() {
        when(hotelRepository.findAll()).thenReturn(List.of());

        List<HotelInfoDto> actualHotels = hotelService.getAllHotels();

        assertThat(actualHotels).isEmpty();

        verify(hotelRepository, times(1)).findAll();
    }

    @Test
    void getHotelById_whenHotelIsFound_shouldReturnHotelDetailsDto() {
        when(hotelRepository.findById(HOTEL_ID)).thenReturn(Optional.of(hotel));
        when(hotelMapper.toHotelDetailsDto(hotel)).thenReturn(hotelDetailsDto);

        HotelDetailsDto actual = hotelService.getHotelById(HOTEL_ID);

        assertThat(actual).isEqualTo(hotelDetailsDto);

        verify(hotelRepository, times(1)).findById(HOTEL_ID);
        verify(hotelMapper, times(1)).toHotelDetailsDto(hotel);
    }

    @Test
    void getHotelById_whenHotelIsNotFound_shouldThrowResourceNotFoundException() {
        when(hotelRepository.findById(HOTEL_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hotelService.getHotelById(HOTEL_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(HOTEL_NOT_FOUND_EXCEPTION_MESSAGE + HOTEL_ID);

        verify(hotelRepository, times(1)).findById(HOTEL_ID);
    }

    @Test
    void searchHotels_whenAmenityListIsNotEmpty_shouldReturnHotelInfoDtoList() {
        when(hotelRepository.searchByParams(
                HOTEL_SEARCH_PARAMS.get("name").get(0),
                HOTEL_SEARCH_PARAMS.get("brand").get(0),
                HOTEL_SEARCH_PARAMS.get("city").get(0),
                HOTEL_SEARCH_PARAMS.get("country").get(0)
        )).thenReturn(hotelList);
        when(hotelMapper.toHotelInfoDto(hotel)).thenReturn(hotelInfoDto);

        List<HotelInfoDto> actual = hotelService.searchHotels(
                HOTEL_SEARCH_PARAMS.get("name").get(0),
                HOTEL_SEARCH_PARAMS.get("brand").get(0),
                HOTEL_SEARCH_PARAMS.get("city").get(0),
                HOTEL_SEARCH_PARAMS.get("country").get(0),
                HOTEL_SEARCH_PARAMS.get("amenities")
        );

        assertThat(actual).isEqualTo(hotelInfoDtoList);

        verify(hotelRepository, times(1)).searchByParams(
                HOTEL_SEARCH_PARAMS.get("name").get(0),
                HOTEL_SEARCH_PARAMS.get("brand").get(0),
                HOTEL_SEARCH_PARAMS.get("city").get(0),
                HOTEL_SEARCH_PARAMS.get("country").get(0)
        );
        verify(hotelMapper, times(1)).toHotelInfoDto(hotel);
    }

    @Test
    void searchHotels_whenAmenityListIsEmpty_shouldReturnHotelInfoDtoList() {
        when(hotelRepository.searchByParams(
                HOTEL_SEARCH_PARAMS.get("name").get(0),
                HOTEL_SEARCH_PARAMS.get("brand").get(0),
                HOTEL_SEARCH_PARAMS.get("city").get(0),
                HOTEL_SEARCH_PARAMS.get("country").get(0)
        )).thenReturn(hotelList);
        when(hotelMapper.toHotelInfoDto(hotel)).thenReturn(hotelInfoDto);

        List<HotelInfoDto> actual = hotelService.searchHotels(
                HOTEL_SEARCH_PARAMS.get("name").get(0),
                HOTEL_SEARCH_PARAMS.get("brand").get(0),
                HOTEL_SEARCH_PARAMS.get("city").get(0),
                HOTEL_SEARCH_PARAMS.get("country").get(0),
                List.of()
        );

        assertThat(actual).isEqualTo(hotelInfoDtoList);

        verify(hotelRepository, times(1)).searchByParams(
                HOTEL_SEARCH_PARAMS.get("name").get(0),
                HOTEL_SEARCH_PARAMS.get("brand").get(0),
                HOTEL_SEARCH_PARAMS.get("city").get(0),
                HOTEL_SEARCH_PARAMS.get("country").get(0)
        );
        verify(hotelMapper, times(1)).toHotelInfoDto(hotel);
    }

    @Test
    void createHotel_whenDataIsValid_shouldReturnHotelInfoDto() {
        when(hotelMapper.toHotel(createHotelDto)).thenReturn(hotel);
        when(hotelRepository.save(hotel)).thenReturn(hotel);
        when(hotelMapper.toHotelInfoDto(hotel)).thenReturn(hotelInfoDto);

        HotelInfoDto actual = hotelService.createHotel(createHotelDto);

        assertThat(actual).isEqualTo(hotelInfoDto);

        verify(hotelMapper, times(1)).toHotel(createHotelDto);
        verify(hotelRepository, times(1)).save(hotel);
        verify(hotelMapper, times(1)).toHotelInfoDto(hotel);
    }

    @Test
    void addAmenitiesToHotel_whenAmenityListIsNotEmpty_shouldReturnNothing() {
        when(hotelRepository.findById(HOTEL_ID)).thenReturn(Optional.of(hotel));
        when(amenityRepository.findByTitle(anyString())).thenReturn(hotel.getAmenities().stream().findAny());

        assertThatNoException().isThrownBy(() -> hotelService.addAmenitiesToHotel(HOTEL_ID, AMENITIES));

        verify(hotelRepository, times(1)).findById(HOTEL_ID);
        verify(amenityRepository, times(3)).findByTitle(anyString());
    }

    @Test
    void addAmenitiesToHotel_whenAmenityListIsEmpty_shouldReturnNothing() {
        when(hotelRepository.findById(HOTEL_ID)).thenReturn(Optional.of(hotel));

        assertThatNoException().isThrownBy(() -> hotelService.addAmenitiesToHotel(HOTEL_ID, List.of()));

        verify(hotelRepository, times(1)).findById(HOTEL_ID);
    }

    @Test
    void getHotelCountGroupedByParameter_whenHistogramParameterIsValid_shouldReturnHistogramMap() {
        when(histogramFactory.getStrategy(HistogramParam.BRAND)).thenReturn(brandHistogramStrategy);
        when(brandHistogramStrategy.generateHistogram()).thenReturn(BRAND_HISTOGRAM);

        Map<String, Long> actual = hotelService.getHotelCountGroupedByParameter(HISTOGRAM_PARAM);

        assertThat(actual).isEqualTo(BRAND_HISTOGRAM);

        verify(histogramFactory, times(1)).getStrategy(HistogramParam.BRAND);
        verify(brandHistogramStrategy, times(1)).generateHistogram();
    }

    @Test
    void getHotelCountGroupedByParameter_whenHistogramParameterIsInvalid_shouldThrowInvalidParameterException() {
        assertThatThrownBy(() -> hotelService.getHotelCountGroupedByParameter(INVALID_HOTEL_ID))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage(INVALID_HISTOGRAM_PARAMETER_EXCEPTION_MESSAGE + INVALID_HOTEL_ID);
    }
}
