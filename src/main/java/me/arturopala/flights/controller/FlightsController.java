package me.arturopala.flights.controller;

import me.arturopala.flights.dto.FlightDto;
import me.arturopala.flights.model.Airport;
import me.arturopala.flights.service.FlightsService;
import me.arturopala.flights.utils.AirportFormatter;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiQueryParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.support.FormatterPropertyEditorAdapter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

@RestController
@RequestMapping("/interconnections")
public class FlightsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightsController.class);

    @Autowired
    private FlightsService flightsService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Airport.class, new FormatterPropertyEditorAdapter(new AirportFormatter()));
    }

    @ApiMethod(description = "Serves information about possible direct and interconnected flights (maximum 1 stop)")
    @RequestMapping(value = "" , method = RequestMethod.GET)
    public ResponseEntity<List<FlightDto>> searchFlights (
        @ApiQueryParam(name = "departure", description = "a departure airport IATA code")
        @RequestParam("departure")
            Airport departure,
        @ApiQueryParam(name = "arrival", description = "an arrival airport IATA code")
        @RequestParam("arrival")
            Airport arrival,
        @ApiQueryParam(name = "departureDateTime", description = "a departure datetime in the departure airport timezone in ISO format")
        @RequestParam("departureDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime departureDateTime,
        @ApiQueryParam(name = "arrivalDateTime", description = "an arrival datetime in the arrival airport timezone in ISO format")
        @RequestParam("arrivalDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime arrivalDateTime
    ) {
        LocalDateTime start = truncate(departureDateTime);
        LocalDateTime end = truncate(arrivalDateTime);
        List<FlightDto> flights = flightsService.findFlights(departure, arrival, start, end, 1, 120, 1440);
        LOGGER.info("Flights departure={}, arrival={}, departureTime={}, arrivalTime={}, flights={}", departure, arrival, start, end, flights);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(APPLICATION_JSON_UTF8);
        return new ResponseEntity<>(flights, responseHeaders, HttpStatus.OK);
    }

    private LocalDateTime truncate(LocalDateTime dateTime) {
        return dateTime.truncatedTo(ChronoUnit.MINUTES);
    }
}
