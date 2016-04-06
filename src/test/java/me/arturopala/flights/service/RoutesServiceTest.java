package me.arturopala.flights.service;

import me.arturopala.flights.TestApplication;
import me.arturopala.flights.UnitTest;
import me.arturopala.flights.model.Airport;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;

import static me.arturopala.flights.Airports.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

@SpringApplicationConfiguration(TestApplication.class)
public class RoutesServiceTest extends UnitTest {

    @Autowired
    private RoutesService routesService;

    @Test
    public void shouldReadRoutesAndReturnConnectionsGraph() throws Exception {
        DirectedGraph<Airport, DefaultEdge> routes = routesService.getAllAvailableRoutes();
        assertThat(routes.vertexSet(), contains(WRO,DUB,STN,VGO,ALC,SXF));
        assertThat(routes.containsEdge(WRO,DUB), is(true));
        assertThat(routes.containsEdge(WRO,STN), is(true));
        assertThat(routes.containsEdge(STN,WRO), is(true));
        assertThat(routes.containsEdge(STN,DUB), is(true));
        assertThat(routes.containsEdge(DUB,WRO), is(true));
        assertThat(routes.containsEdge(WRO,VGO), is(false));
        assertThat(routes.containsEdge(VGO,WRO), is(false));
    }
}

