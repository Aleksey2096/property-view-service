package com.example.propertyview.controller;

import com.example.propertyview.controller.dto.CreateHotelDto;
import com.example.propertyview.controller.dto.HotelDetailsDto;
import com.example.propertyview.controller.dto.HotelInfoDto;
import com.example.propertyview.exception.InvalidParameterException;
import com.example.propertyview.exception.ResponseExceptionBody;
import com.example.propertyview.service.HotelService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

import static com.example.propertyview.TestUtil.ADD_AMENITIES_CONTROLLER_PATH;
import static com.example.propertyview.TestUtil.BRAND_HISTOGRAM;
import static com.example.propertyview.TestUtil.FAILED_TO_CONVERT_TO_LONG_EXCEPTION_MESSAGE;
import static com.example.propertyview.TestUtil.HISTOGRAM_CONTROLLER_PATH;
import static com.example.propertyview.TestUtil.HISTOGRAM_PARAM;
import static com.example.propertyview.TestUtil.HOTEL_CONTROLLER_PATH;
import static com.example.propertyview.TestUtil.HOTEL_ID;
import static com.example.propertyview.TestUtil.HOTEL_SEARCH_PARAMS;
import static com.example.propertyview.TestUtil.INVALID_CREATE_HOTEL_DTO;
import static com.example.propertyview.TestUtil.INVALID_HISTOGRAM_PARAMETER_EXCEPTION_MESSAGE;
import static com.example.propertyview.TestUtil.INVALID_HOTEL_ID;
import static com.example.propertyview.TestUtil.MISSING_REQUIRED_ATTRIBUTE_EXCEPTION_MESSAGE;
import static com.example.propertyview.TestUtil.OBJECT_MAPPER;
import static com.example.propertyview.TestUtil.SEARCH_HOTELS_CONTROLLER_PATH;
import static com.example.propertyview.TestUtil.TIMESTAMP_ATTRIBUTE;
import static com.example.propertyview.TestUtil.AMENITIES;
import static com.example.propertyview.TestUtil.generateCreateHotelDto;
import static com.example.propertyview.TestUtil.generateHotelDetailsDto;
import static com.example.propertyview.TestUtil.generateHotelInfoDto;
import static com.example.propertyview.TestUtil.generateMockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class HotelControllerTest {
    @InjectMocks
    private HotelController hotelController;

    @Mock
    private HotelService hotelService;

    private MockMvc mockMvc;
    private final CreateHotelDto createHotelDto = generateCreateHotelDto();
    private final HotelInfoDto hotelInfoDto = generateHotelInfoDto();
    private final List<HotelInfoDto> hotelInfoDtoList = List.of(hotelInfoDto);
    private final HotelDetailsDto hotelDetailsDto = generateHotelDetailsDto();

    @BeforeEach
    void setUp() {
        mockMvc = generateMockMvc(hotelController);
    }

    @Test
    void getAllHotels_whenHotelsExist_shouldReturnAllHotels() throws Exception {
        when(hotelService.getAllHotels()).thenReturn(hotelInfoDtoList);

        testGetRequest(HOTEL_CONTROLLER_PATH, Map.of(), hotelInfoDtoList, status().isOk());

        verify(hotelService, times(1)).getAllHotels();
    }

    @Test
    void getAllHotels_whenHotelsDoNotExist_shouldReturnEmptyList() throws Exception {
        when(hotelService.getAllHotels()).thenReturn(List.of());

        testGetRequest(HOTEL_CONTROLLER_PATH, Map.of(), List.of(), status().isOk());

        verify(hotelService, times(1)).getAllHotels();
    }

    @Test
    void getHotelById_whenHotelExists_shouldReturnHotelDetailsDto() throws Exception {
        when(hotelService.getHotelById(HOTEL_ID)).thenReturn(hotelDetailsDto);

        testGetRequest(HOTEL_CONTROLLER_PATH + File.separator + HOTEL_ID, Map.of(), hotelDetailsDto, status().isOk());

        verify(hotelService, times(1)).getHotelById(HOTEL_ID);
    }

    @Test
    void getHotelById_whenHotelIdIsInvalid_shouldReturnBadRequest() throws Exception {
        String getHotelByIdUrl = HOTEL_CONTROLLER_PATH + File.separator + INVALID_HOTEL_ID;
        ResponseExceptionBody responseExceptionBody = new ResponseExceptionBody(
                BAD_REQUEST.value(),
                String.format(FAILED_TO_CONVERT_TO_LONG_EXCEPTION_MESSAGE, INVALID_HOTEL_ID),
                getHotelByIdUrl
        );

        testGetRequest(getHotelByIdUrl, Map.of(), responseExceptionBody, status().isBadRequest());
    }

    @Test
    void searchHotels_whenRequiredHotelsExist_shouldReturnHotelsList() throws Exception {
        when(hotelService.searchHotels(
                HOTEL_SEARCH_PARAMS.get("name").get(0),
                HOTEL_SEARCH_PARAMS.get("brand").get(0),
                HOTEL_SEARCH_PARAMS.get("city").get(0),
                HOTEL_SEARCH_PARAMS.get("country").get(0),
                HOTEL_SEARCH_PARAMS.get("amenities")))
                .thenReturn(hotelInfoDtoList);

        testGetRequest(SEARCH_HOTELS_CONTROLLER_PATH, HOTEL_SEARCH_PARAMS, hotelInfoDtoList, status().isOk());

        verify(hotelService, times(1)).searchHotels(
                HOTEL_SEARCH_PARAMS.get("name").get(0),
                HOTEL_SEARCH_PARAMS.get("brand").get(0),
                HOTEL_SEARCH_PARAMS.get("city").get(0),
                HOTEL_SEARCH_PARAMS.get("country").get(0),
                HOTEL_SEARCH_PARAMS.get("amenities"));
    }

    @Test
    void searchHotels_whenRequiredHotelsDoNotExist_shouldReturnEmptyList() throws Exception {
        when(hotelService.searchHotels(
                HOTEL_SEARCH_PARAMS.get("name").get(0),
                HOTEL_SEARCH_PARAMS.get("brand").get(0),
                HOTEL_SEARCH_PARAMS.get("city").get(0),
                HOTEL_SEARCH_PARAMS.get("country").get(0),
                HOTEL_SEARCH_PARAMS.get("amenities")))
                .thenReturn(List.of());

        testGetRequest(SEARCH_HOTELS_CONTROLLER_PATH, HOTEL_SEARCH_PARAMS, List.of(), status().isOk());

        verify(hotelService, times(1)).searchHotels(
                HOTEL_SEARCH_PARAMS.get("name").get(0),
                HOTEL_SEARCH_PARAMS.get("brand").get(0),
                HOTEL_SEARCH_PARAMS.get("city").get(0),
                HOTEL_SEARCH_PARAMS.get("country").get(0),
                HOTEL_SEARCH_PARAMS.get("amenities"));
    }

    @Test
    void createHotel_whenDataIsValid_shouldReturnHotelInfoDto() throws Exception {
        when(hotelService.createHotel(any(CreateHotelDto.class))).thenReturn(hotelInfoDto);

        testPostRequest(HOTEL_CONTROLLER_PATH, createHotelDto, hotelInfoDto, status().isCreated());

        verify(hotelService, times(1)).createHotel(any(CreateHotelDto.class));
    }

    @Test
    void createHotel_whenRequiredAttributeIsNull_shouldReturnBadRequest() throws Exception {
        ResponseExceptionBody responseExceptionBody = new ResponseExceptionBody(
                BAD_REQUEST.value(),
                MISSING_REQUIRED_ATTRIBUTE_EXCEPTION_MESSAGE,
                HOTEL_CONTROLLER_PATH
        );

        testPostRequest(HOTEL_CONTROLLER_PATH, INVALID_CREATE_HOTEL_DTO, responseExceptionBody, status().isBadRequest());
    }

    @Test
    void addAmenities_whenDataIsValid_shouldReturnNoContentStatus() throws Exception {
        doNothing().when(hotelService).addAmenitiesToHotel(HOTEL_ID, AMENITIES);

        mockMvc.perform(post(String.format(ADD_AMENITIES_CONTROLLER_PATH, HOTEL_ID))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_MAPPER.writeValueAsString(AMENITIES)))
                .andExpect(status().isNoContent());

        verify(hotelService, times(1)).addAmenitiesToHotel(HOTEL_ID, AMENITIES);
    }

    @Test
    void addAmenities_whenHotelIdIsInvalid_shouldReturnBadRequest() throws Exception {
        ResponseExceptionBody responseExceptionBody = new ResponseExceptionBody(
                BAD_REQUEST.value(),
                String.format(FAILED_TO_CONVERT_TO_LONG_EXCEPTION_MESSAGE, INVALID_HOTEL_ID),
                String.format(ADD_AMENITIES_CONTROLLER_PATH, INVALID_HOTEL_ID)
        );

        testPostRequest(responseExceptionBody.getPath(), AMENITIES, responseExceptionBody, status().isBadRequest());
    }

    @Test
    void getHistogram_whenHistogramParamExists_shouldReturnHistogram() throws Exception {
        when(hotelService.getHotelCountGroupedByParameter(HISTOGRAM_PARAM)).thenReturn(BRAND_HISTOGRAM);

        testGetRequest(HISTOGRAM_CONTROLLER_PATH + File.separator + HISTOGRAM_PARAM, Map.of(), BRAND_HISTOGRAM, status().isOk());

        verify(hotelService, times(1)).getHotelCountGroupedByParameter(HISTOGRAM_PARAM);
    }

    @Test
    void getHistogram_whenHistogramParamDoesNotExist_shouldReturnBadRequest() throws Exception {
        when(hotelService.getHotelCountGroupedByParameter(HISTOGRAM_PARAM))
                .thenThrow(new InvalidParameterException(INVALID_HISTOGRAM_PARAMETER_EXCEPTION_MESSAGE + HISTOGRAM_PARAM));

        ResponseExceptionBody responseExceptionBody = new ResponseExceptionBody(
                BAD_REQUEST.value(),
                INVALID_HISTOGRAM_PARAMETER_EXCEPTION_MESSAGE + HISTOGRAM_PARAM,
                HISTOGRAM_CONTROLLER_PATH + File.separator + HISTOGRAM_PARAM
        );

        testGetRequest(responseExceptionBody.getPath(), Map.of(), responseExceptionBody, status().isBadRequest());

        verify(hotelService, times(1)).getHotelCountGroupedByParameter(HISTOGRAM_PARAM);
    }

    private void testGetRequest(String url, Map<String, List<String>> params, Object receivedContent, ResultMatcher expectedStatus) throws Exception {
        String receivedContentJson = writeValueAsStringWithoutTimestamp(receivedContent);

        mockMvc.perform(get(url)
                        .params(CollectionUtils.toMultiValueMap(params)))
                .andExpect(expectedStatus)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(receivedContentJson));
    }

    private void testPostRequest(String url, Object sentContent, Object receivedContent, ResultMatcher expectedStatus) throws Exception {
        String sentContentJson = OBJECT_MAPPER.writeValueAsString(sentContent);
        String receivedContentJson = writeValueAsStringWithoutTimestamp(receivedContent);

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(sentContentJson))
                .andExpect(expectedStatus)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(receivedContentJson));
    }

    private String writeValueAsStringWithoutTimestamp(Object value) throws Exception {
        if (value instanceof List<?>) {
            return OBJECT_MAPPER.writeValueAsString(value);
        }
        Map<String, Object> expectedMap = OBJECT_MAPPER.convertValue(value, new TypeReference<>() {
        });
        expectedMap.remove(TIMESTAMP_ATTRIBUTE);
        return OBJECT_MAPPER.writeValueAsString(expectedMap);
    }
}
