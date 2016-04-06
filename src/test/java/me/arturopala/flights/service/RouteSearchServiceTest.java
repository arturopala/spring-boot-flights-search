package me.arturopala.flights.service;

import me.arturopala.flights.TestApplication;
import me.arturopala.flights.UnitTest;
import me.arturopala.flights.model.Route;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;

import java.util.Arrays;
import java.util.List;

import static me.arturopala.flights.Airports.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringApplicationConfiguration(TestApplication.class)
public class RouteSearchServiceTest extends UnitTest {

    @Autowired
    private RouteSearchService routeSearchService;

    @Test
    public void shouldFindDirectRoute() throws Exception {
        List<List<Route>> routes = routeSearchService.findRoutesBetween(WRO, DUB, 0);
        assertThat(routes, hasSize(1));
        assertThat(routes, containsInAnyOrder(
            Arrays.asList(Route.of(WRO,DUB))
        ));
    }

    @Test
    public void shouldFind2LegRoutesIfAny() throws Exception {
        List<List<Route>> routes = routeSearchService.findRoutesBetween(WRO, VGO, 1);
        assertThat(routes, hasSize(2));
        assertThat(routes, containsInAnyOrder(
            Arrays.asList(Route.of(WRO,DUB), Route.of(DUB,VGO)),
            Arrays.asList(Route.of(WRO,STN), Route.of(STN,VGO))
        ));
    }

    @Test
    public void shouldFind3LegRoutesIfAny() throws Exception {
        List<List<Route>> routes = routeSearchService.findRoutesBetween(WRO, ALC, 2);
        assertThat(routes, hasSize(2));
        assertThat(routes, containsInAnyOrder(
            Arrays.asList(Route.of(WRO,DUB), Route.of(DUB,VGO), Route.of(VGO,ALC)),
            Arrays.asList(Route.of(WRO,STN), Route.of(STN,VGO), Route.of(VGO,ALC))
        ));
    }

    @Test
    public void shouldFindMultiLegRoutes() throws Exception {
        List<List<Route>> routes = routeSearchService.findRoutesBetween(WRO, DUB, 1);
        assertThat(routes, hasSize(2));
        assertThat(routes, containsInAnyOrder(
            Arrays.asList(Route.of(WRO,DUB)),
            Arrays.asList(Route.of(WRO,STN), Route.of(STN,DUB))
        ));
    }

    @Test
    public void shouldNotFindDirectRoutesIfNotExist() throws Exception {
        List<List<Route>> routes = routeSearchService.findRoutesBetween(WRO, VGO, 0);
        assertThat(routes, empty());
    }

    @Test
    public void shouldNotFind2LegRoutesIfNotExist() throws Exception {
        List<List<Route>> routes = routeSearchService.findRoutesBetween(WRO, SXF, 1);
        assertThat(routes, empty());
    }

    @Test
    public void shouldNotFindRouteBetweenSameAirports() throws Exception {
        assertThat(routeSearchService.findRoutesBetween(WRO, WRO, 1), empty());
        assertThat(routeSearchService.findRoutesBetween(DUB, DUB, 1), empty());
        assertThat(routeSearchService.findRoutesBetween(STN, STN, 0), empty());
        assertThat(routeSearchService.findRoutesBetween(VGO, VGO, 0), empty());
    }

    @Test
    public void shouldNotFindRouteForNegativeStops() throws Exception {
        assertThat(routeSearchService.findRoutesBetween(WRO, DUB, -1), empty());
        assertThat(routeSearchService.findRoutesBetween(DUB, SXF, -2), empty());
        assertThat(routeSearchService.findRoutesBetween(VGO, WRO, -3), empty());
        assertThat(routeSearchService.findRoutesBetween(STN, VGO, -4), empty());
    }
}
