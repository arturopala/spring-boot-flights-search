package me.arturopala.flights.service;

import me.arturopala.flights.dto.FlightDto;
import me.arturopala.flights.model.Airport;
import java.time.LocalDateTime;
import java.util.List;

public interface FlightsService {

    List<FlightDto> findFlights(Airport departure, Airport arrival, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime, int maxStops, int transferMinutes, int maxTransferMinutes);
}
