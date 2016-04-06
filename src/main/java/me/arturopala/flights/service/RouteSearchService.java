package me.arturopala.flights.service;

import me.arturopala.flights.model.Airport;
import me.arturopala.flights.model.Route;

import java.util.List;

public interface RouteSearchService {

    List<List<Route>> findRoutesBetween(Airport departure, Airport arrival, int maxStops);
}
