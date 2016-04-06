package me.arturopala.flights.integration;

import me.arturopala.flights.UnitTest;
import me.arturopala.flights.dto.RouteDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RoutesApiClientTest extends UnitTest {

    @Autowired
    private RoutesApiClient client;

    @Test
    public void shouldGetRoutes() {
        List<RouteDto> routes = client.getRoutes();
        assertThat(routes.size(),greaterThan(0));
        routes.stream().forEach( r -> {
            assertThat(r.getAirportFrom(), not(nullValue()));
            assertThat(r.getAirportTo(), not(nullValue()));
            assertThat(r.getAirportTo().getIataCode().length(), is(3));
            assertThat(r.getAirportFrom().getIataCode().length(), is(3));
            assertThat(r.getAirportFrom(),not(equalTo(r.getAirportTo())));
        });
    }

}
