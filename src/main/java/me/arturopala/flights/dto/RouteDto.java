package me.arturopala.flights.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import me.arturopala.flights.model.Airport;
import me.arturopala.flights.model.Route;

import java.io.Serializable;

public class RouteDto implements Serializable {

    private final Route route;
    private final Boolean newRoute;
    private final Boolean seasonalRoute;

    public static RouteDto of(Airport airportFrom, Airport airportTo) {
        return new RouteDto(airportFrom, airportTo, false, false);
    }

    @JsonCreator
    public RouteDto(@JsonProperty("airportFrom") Airport airportFrom,
                    @JsonProperty("airportTo") Airport airportTo,
                    @JsonProperty("newRoute") Boolean newRoute,
                    @JsonProperty("seasonalRoute") Boolean seasonalRoute) {
        this.route = Route.of(airportFrom,airportTo);
        this.newRoute = newRoute;
        this.seasonalRoute = seasonalRoute;
    }

    public Route getRoute() {
        return route;
    }

    public Boolean getNewRoute() {
        return newRoute;
    }

    public Boolean getSeasonalRoute() {
        return seasonalRoute;
    }

    public Airport getAirportFrom() {
        return route.getFrom();
    }

    public Airport getAirportTo() {
        return route.getTo();
    }
}
