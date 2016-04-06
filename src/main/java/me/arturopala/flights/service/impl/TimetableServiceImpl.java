package me.arturopala.flights.service.impl;

import me.arturopala.flights.Constants;
import me.arturopala.flights.integration.SchedulesApiClient;
import me.arturopala.flights.model.Airport;
import me.arturopala.flights.model.Flight;
import me.arturopala.flights.model.Route;
import me.arturopala.flights.model.Timetable;
import me.arturopala.flights.service.TimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Cacheable(Constants.CACHE_TIMETABLES)
public class TimetableServiceImpl implements TimetableService {

    @Autowired
    private SchedulesApiClient schedulesApiClient;

    @Override
    public Optional<Timetable> getFlightsTimetable(Airport departure, Airport arrival, YearMonth yearMonth) {
        Route route = Route.of(departure,arrival);
        return schedulesApiClient
            .getSchedules(departure, arrival, yearMonth)
            .map(
                schedules -> {
                    if (schedules.getMonth() != yearMonth.getMonthValue()) throw new IllegalStateException(
                        "Wrong schedule received from Schedules API, expected " + yearMonth.getMonthValue() + " but got " + schedules.getMonth()
                    );
                    return schedules
                        .getDays()
                        .stream()
                        .flatMap(
                            day -> {
                                LocalDate date = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), day.getDay());
                                return day
                                    .getFlights()
                                    .stream()
                                    .map(c -> new Flight(
                                        c.getNumber(),
                                        route,
                                        c.getDepartureTime().atDate(date),
                                        c.getArrivalTime().atDate(date)));
                            }
                        )
                        .sorted(Flight.FLIGHT_DEPARTURE_TIME_COMPARATOR)
                        .collect(Collectors.toList());
            })
            .map(flights -> new Timetable(route,yearMonth, flights));
    }
}
