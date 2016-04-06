package me.arturopala.flights.model;

import me.arturopala.flights.UnitTest;
import org.junit.Test;

import javax.validation.ValidationException;

import static me.arturopala.flights.Airports.DUB;
import static me.arturopala.flights.Airports.WRO;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RouteTest extends UnitTest {

    @Test
    public void shouldCreateNewRoutes() throws Exception {
        Route route = Route.of(WRO, DUB);
        assertThat(route, not(nullValue()));
        assertThat(route.getFrom(), is(WRO));
        assertThat(route.getTo(), is(DUB));
    }

    @Test
    public void shouldEqualWhenSameFromAndTo() throws Exception {
        assertThat(Route.of(WRO,DUB), is(Route.of(WRO,DUB)));
    }

    @Test
    public void shouldNotEqualWhenReversedFromAndTo() throws Exception {
        assertThat(Route.of(WRO,DUB), not(Route.of(DUB,WRO)));
    }

    @Test
    public void shouldCachedValues() throws Exception {
        assertThat(Route.of(WRO,DUB), sameInstance(Route.of(WRO,DUB)));
    }

    @Test(expected = ValidationException.class)
    public void shouldNotCreateRouteWhenFromIsSameAsTo() throws Exception {
        Route.of(DUB,DUB);
    }

    @Test(expected = ValidationException.class)
    public void shouldNotCreateRouteWhenFromIsNull() throws Exception {
        Route.of(null,DUB);
    }

    @Test(expected = ValidationException.class)
    public void shouldNotCreateRouteWhenToIsNull() throws Exception {
        Route.of(WRO,null);
    }

    @Test(expected = ValidationException.class)
    public void shouldNotCreateRouteWhenFromAndToIsNull() throws Exception {
        Route.of(null,null);
    }

}
