package me.arturopala.flights.service.impl;

import me.arturopala.flights.Constants;
import me.arturopala.flights.model.Airport;
import me.arturopala.flights.model.Route;
import me.arturopala.flights.service.RoutesService;
import org.jgrapht.DirectedGraph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.AllDirectedPaths;
import org.jgrapht.graph.DefaultEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@Component
public class RouteSearchServiceImpl implements me.arturopala.flights.service.RouteSearchService {

    @Autowired
    private RoutesService routesService;

    @Override
    @Cacheable(Constants.CACHE_ROUTES_SEARCHES)
    public List<List<Route>> findRoutesBetween(Airport departure, Airport arrival, int maxStops){
        DirectedGraph<Airport, DefaultEdge> routes = routesService.getAllAvailableRoutes();
        if(departure.equals(arrival) || maxStops < 0) return emptyList();
        List<List<Route>> result = new LinkedList<>();
        if (routes.containsEdge(departure, arrival)) {
            result.add(singletonList(Route.of(departure, arrival)));
        }
        if (maxStops > 0){
            List<List<Route>> multiLegRoutes = findMultiLegRoutes(departure, arrival, maxStops, routes);
            result.addAll(multiLegRoutes);
        }
        return result;
    }

    private List<List<Route>> findMultiLegRoutes(Airport departure, Airport arrival, int maxStops, DirectedGraph<Airport, DefaultEdge> routes) {
        AllDirectedPaths pathFinder = new AllDirectedPaths<>(routes);
        List<GraphPath<Airport, DefaultEdge>> paths = pathFinder.getAllPaths(departure, arrival, true, maxStops + 1);
        return paths.stream().map(path -> {
            List<Airport> airportList = Graphs.getPathVertexList(path);
            if (airportList.size() == 2)
                return singletonList(Route.of(airportList.get(0), airportList.get(1)));
            else {
                List<Route> legs = new LinkedList<>();
                for (int i = 0; i < airportList.size() - 1; i++) {
                    legs.add(Route.of(airportList.get(i), airportList.get(i + 1)));
                }
                return legs;
            }

        }).collect(Collectors.toList());
    }
}
