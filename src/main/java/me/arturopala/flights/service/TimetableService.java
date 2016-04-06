package me.arturopala.flights.service;

import me.arturopala.flights.dto.SchedulesDto;
import me.arturopala.flights.model.Airport;
import me.arturopala.flights.model.Timetable;

import java.time.YearMonth;
import java.util.Optional;

public interface TimetableService {

    Optional<Timetable> getFlightsTimetable(Airport departure, Airport arrival, YearMonth yearMonth);
}
