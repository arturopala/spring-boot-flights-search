package me.arturopala.flights.service.impl;

import me.arturopala.flights.Constants;
import me.arturopala.flights.dto.RouteDto;
import me.arturopala.flights.integration.RoutesApiClient;
import me.arturopala.flights.model.Airport;
import me.arturopala.flights.service.RoutesService;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoutesServiceImpl implements RoutesService {

    @Autowired
    private RoutesApiClient routesApiClient;

    @Override
    @Cacheable(Constants.CACHE_ROUTES)
    public DirectedGraph<Airport, DefaultEdge> getAllAvailableRoutes() {
        DirectedGraph<Airport, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);
        List<RouteDto> routes = routesApiClient.getRoutes();
        routes.stream().forEach( route -> {
            graph.addVertex(route.getAirportFrom());
            graph.addVertex(route.getAirportTo());
            graph.addEdge(route.getAirportFrom(), route.getAirportTo());
        });
        return graph;
    }
}
