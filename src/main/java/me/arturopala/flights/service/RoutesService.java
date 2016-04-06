package me.arturopala.flights.service;

import me.arturopala.flights.model.Airport;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public interface RoutesService {

    DirectedGraph<Airport, DefaultEdge> getAllAvailableRoutes();

}
