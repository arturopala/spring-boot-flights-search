package me.arturopala.flights.service.impl;

import me.arturopala.flights.Constants;
import me.arturopala.flights.dto.FlightDto;
import me.arturopala.flights.model.Airport;
import me.arturopala.flights.model.Flight;
import me.arturopala.flights.model.Route;
import me.arturopala.flights.service.FlightsService;
import me.arturopala.flights.service.RouteSearchService;
import me.arturopala.flights.service.TimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Component
public class FlightsServiceImpl implements FlightsService {

    @Autowired
    private RouteSearchService routeSearchService;

    @Autowired
    private TimetableService timetableService;

    @Override
    @Cacheable(Constants.CACHE_FLIGHTS)
    public List<FlightDto> findFlights(
        Airport departure,
        Airport arrival,
        LocalDateTime departureDateTime,
        LocalDateTime arrivalDateTime,
        int maxStops,
        int minTransferMinutes,
        int maxTransferMinutes) {

        if(departure.equals(arrival)) return emptyList();
        if(departureDateTime.compareTo(arrivalDateTime) >= 0) return emptyList();

        List<YearMonth> months = getMonthsBetween(departureDateTime, arrivalDateTime);
        List<List<Route>> routingList = routeSearchService.findRoutesBetween(departure, arrival, maxStops);

        BiFunction<List<List<Flight>>, List<Flight>, List<List<Flight>>> lookupLegs = getLookupNextLegs(arrivalDateTime,minTransferMinutes,maxTransferMinutes);

        return routingList.stream()
            .flatMap(routing -> {
                List<List<Flight>> flightsPerLeg = getFlightsPerLeg(departureDateTime, arrivalDateTime, months, routing);
                List<List<Flight>> legsList = initializeLegList(flightsPerLeg);

                if(flightsPerLeg.size()>1) {
                    for (List<Flight> nextLegFlights : flightsPerLeg.subList(1, flightsPerLeg.size())) {
                        legsList = lookupLegs.apply(legsList, nextLegFlights);
                    }
                }

                return legsList.stream().map(legs -> new FlightDto(legs.size()-1, legs));
            })
            .collect(Collectors.toList());
    }

    public List<List<Flight>> initializeLegList(List<List<Flight>> flightsPerLeg) {
        return flightsPerLeg.get(0).stream()
            .map(Collections::singletonList)
            .collect(Collectors.toList());
    }

    public List<List<Flight>> getFlightsPerLeg(LocalDateTime departureDateTime, LocalDateTime arrivalDateTime, List<YearMonth> months, List<Route> routing) {
        return routing.stream()
            .map(route -> getFlights(route.getFrom(), route.getTo(), months, departureDateTime, arrivalDateTime))
            .collect(Collectors.toList());
    }

    public BiFunction<List<List<Flight>>, List<Flight>, List<List<Flight>>> getLookupNextLegs(
        LocalDateTime maxArrivalDateTime,
        int minTransferMinutes,
        int maxTransferMinutes
    ){
        return (
            List<List<Flight>> currentLegsList,
            List<Flight> nextLegFlights
        ) -> currentLegsList.stream().flatMap(currentLegs -> {
            Flight lastLeg = currentLegs.get(currentLegs.size() - 1);
            LocalDateTime earliestDeparture = lastLeg.getArrivalTime().plusMinutes(minTransferMinutes);
            LocalDateTime latestDeparture = lastLeg.getArrivalTime().plusMinutes(maxTransferMinutes);
            return nextLegFlights.stream()
                .filter(flight -> !flight.getDepartureTime().isBefore(earliestDeparture)
                               && !flight.getDepartureTime().isAfter(latestDeparture)
                               && !flight.getArrivalTime().isAfter(maxArrivalDateTime)
                )
                .map(flight -> {
                    List<Flight> newLegs = new LinkedList<>(currentLegs);
                    newLegs.add(flight);
                    return newLegs;
                });
        })
        .collect(Collectors.toList());
    }

    public List<Flight> getFlights(Airport departure, Airport arrival, List<YearMonth> months, LocalDateTime from, LocalDateTime to) {
        return months.parallelStream()
            .map(month -> timetableService.getFlightsTimetable(departure, arrival, month))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .flatMap(t -> t.getFlights().stream())
            .filter(f -> !f.getDepartureTime().isBefore(from) && !f.getArrivalTime().isAfter(to))
            .collect(Collectors.toList());
    }

    public static List<YearMonth> getMonthsBetween(LocalDateTime start, LocalDateTime end) {
        if(start.isAfter(end)) {
            List<YearMonth> months = getMonthsBetween(end, start);
            Collections.reverse(months);
            return months;
        } else {
            YearMonth from = YearMonth.from(start);
            YearMonth to = YearMonth.from(end);
            List<YearMonth> months = new LinkedList<>();
            months.add(from);
            if (to.isAfter(from)) {
                YearMonth m = from;
                do {
                    m = m.plusMonths(1);
                    months.add(m);
                }
                while (m.isBefore(to));
            }
            return months;
        }
    }


}
