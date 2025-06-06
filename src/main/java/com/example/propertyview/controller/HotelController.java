package com.example.propertyview.controller;

import com.example.propertyview.controller.dto.CreateHotelDto;
import com.example.propertyview.controller.dto.HotelDetailsDto;
import com.example.propertyview.controller.dto.HotelInfoDto;
import com.example.propertyview.exception.ResponseExceptionBody;
import com.example.propertyview.service.HotelService;
import com.example.propertyview.util.SwaggerMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.propertyview.util.ConstantUtil.JSON_MEDIA_TYPE;

@RestController
@RequestMapping("/property-view")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @GetMapping("/hotels")
    @Operation(summary = "Get a list of all hotels with brief information")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = SwaggerMessage.Code.CODE_200, description = SwaggerMessage.Message.MESSAGE_200,
                    content = @Content(
                            mediaType = JSON_MEDIA_TYPE,
                            schema = @Schema(implementation = HotelInfoDto.class)))
    })
    public ResponseEntity<List<HotelInfoDto>> getAllHotels() {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @GetMapping("/hotels/{id}")
    @Operation(summary = "Get detailed information about a specific hotel by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = SwaggerMessage.Code.CODE_200, description = SwaggerMessage.Message.MESSAGE_200,
                    content = @Content(
                            mediaType = JSON_MEDIA_TYPE,
                            schema = @Schema(implementation = HotelDetailsDto.class))),
            @ApiResponse(
                    responseCode = SwaggerMessage.Code.CODE_400, description = SwaggerMessage.Message.MESSAGE_400,
                    content = @Content(
                            mediaType = JSON_MEDIA_TYPE,
                            schema = @Schema(implementation = ResponseExceptionBody.class))),
            @ApiResponse(
                    responseCode = SwaggerMessage.Code.CODE_404, description = SwaggerMessage.Message.MESSAGE_404,
                    content = @Content(
                            mediaType = JSON_MEDIA_TYPE,
                            schema = @Schema(implementation = ResponseExceptionBody.class)))
    })
    public ResponseEntity<HotelDetailsDto> getHotelById(@Parameter(description = "id of the hotel") @PathVariable Long id) {
        return ResponseEntity.ok(hotelService.getHotelById(id));
    }

    @GetMapping("/search")
    @Operation(summary = "Search hotels by name, brand, city, country, and amenities")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = SwaggerMessage.Code.CODE_200, description = SwaggerMessage.Message.MESSAGE_200,
                    content = @Content(
                            mediaType = JSON_MEDIA_TYPE,
                            schema = @Schema(implementation = HotelInfoDto.class)))
    })
    public ResponseEntity<List<HotelInfoDto>> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) List<String> amenities) {
        return ResponseEntity.ok(hotelService.searchHotels(name, brand, city, country, amenities));
    }

    @PostMapping("/hotels")
    @Operation(summary = "Create a new hotel")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = SwaggerMessage.Code.CODE_201, description = SwaggerMessage.Message.MESSAGE_201,
                    content = @Content(
                            mediaType = JSON_MEDIA_TYPE,
                            schema = @Schema(implementation = HotelInfoDto.class))),
            @ApiResponse(
                    responseCode = SwaggerMessage.Code.CODE_400, description = SwaggerMessage.Message.MESSAGE_400,
                    content = @Content(
                            mediaType = JSON_MEDIA_TYPE,
                            schema = @Schema(implementation = ResponseExceptionBody.class)))
    })
    public ResponseEntity<HotelInfoDto> createHotel(@Valid @RequestBody CreateHotelDto createHotelDto) {
        return new ResponseEntity<>(hotelService.createHotel(createHotelDto), HttpStatus.CREATED);
    }

    @PostMapping("/hotels/{id}/amenities")
    @Operation(summary = "Add amenities to an existing hotel")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = SwaggerMessage.Code.CODE_204, description = SwaggerMessage.Message.MESSAGE_204),
            @ApiResponse(
                    responseCode = SwaggerMessage.Code.CODE_400, description = SwaggerMessage.Message.MESSAGE_400,
                    content = @Content(
                            mediaType = JSON_MEDIA_TYPE,
                            schema = @Schema(implementation = ResponseExceptionBody.class))),
            @ApiResponse(
                    responseCode = SwaggerMessage.Code.CODE_404, description = SwaggerMessage.Message.MESSAGE_404,
                    content = @Content(
                            mediaType = JSON_MEDIA_TYPE,
                            schema = @Schema(implementation = ResponseExceptionBody.class)))
    })
    public ResponseEntity<Void> addAmenities(@Parameter(description = "id of the hotel") @PathVariable Long id,
                                             @RequestBody List<String> amenities) {
        hotelService.addAmenitiesToHotel(id, amenities);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/histogram/{param}")
    @Operation(summary = "Get histogram of hotels grouped by specified parameter")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = SwaggerMessage.Code.CODE_200, description = SwaggerMessage.Message.MESSAGE_200,
                    content = @Content(
                            mediaType = JSON_MEDIA_TYPE,
                            schema = @Schema(implementation = HashMap.class))),
            @ApiResponse(
                    responseCode = SwaggerMessage.Code.CODE_400, description = SwaggerMessage.Message.MESSAGE_400,
                    content = @Content(
                            mediaType = JSON_MEDIA_TYPE,
                            schema = @Schema(implementation = ResponseExceptionBody.class)))
    })
    public ResponseEntity<Map<String, Long>> getHistogram(
            @Parameter(description = "Parameter to group by: brand, city, country, or amenities") @PathVariable String param) {
        return ResponseEntity.ok(hotelService.getHotelCountGroupedByParameter(param));
    }
}
