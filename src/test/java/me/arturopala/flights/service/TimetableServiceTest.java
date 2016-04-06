package me.arturopala.flights.service;

import me.arturopala.flights.TestApplication;
import me.arturopala.flights.UnitTest;
import me.arturopala.flights.model.Flight;
import me.arturopala.flights.model.Route;
import me.arturopala.flights.model.Timetable;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;

import java.time.YearMonth;
import java.util.Optional;

import static java.time.LocalDateTime.of;
import static me.arturopala.flights.Airports.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringApplicationConfiguration(TestApplication.class)
public class TimetableServiceTest extends UnitTest {

    @Autowired
    private TimetableService timetableService;

    @Test
    public void shouldGetFlightsTimetable() throws Exception {
        Route route = Route.of(WRO, DUB);
        Optional<Timetable> timetable = timetableService.getFlightsTimetable(WRO, DUB, YearMonth.of(2016, 5));
        assertThat(timetable, not(Optional.empty()));
        timetable.ifPresent(t -> {
            assertThat(t.getYearMonth(), is(YearMonth.of(2016,5)));
            assertThat(t.getRoute(), is(Route.of(WRO,DUB)));
            assertThat(t.getFlights(), contains(
                new Flight("A001", route, of(2016,5,1,10,15),  of(2016,5,1,11,30)),
                new Flight("A002", route, of(2016,5,1,12,30),  of(2016,5,1,13,45)),
                new Flight("A003", route, of(2016,5,1,16,0),   of(2016,5,1,17,15)),
                new Flight("A004", route, of(2016,5,1,20,45),  of(2016,5,1,21,0)),
                new Flight("A001", route, of(2016,5,3,10,15),  of(2016,5,3,11,30)),
                new Flight("A002", route, of(2016,5,3,12,30),  of(2016,5,3,13,45)),
                new Flight("A001", route, of(2016,5,7,10,15),  of(2016,5,7,11,30)),
                new Flight("A003", route, of(2016,5,7,16,0),   of(2016,5,7,17,15)),
                new Flight("A004", route, of(2016,5,7,20,45),  of(2016,5,7,21,0)),
                new Flight("A004", route, of(2016,5,13,20,45), of(2016,5,13,21,0)),
                new Flight("A003", route, of(2016,5,21,16,0),  of(2016,5,21,17,15)),
                new Flight("A004", route, of(2016,5,21,20,45), of(2016,5,21,21,0)),
                new Flight("A003", route, of(2016,5,25,16,0),  of(2016,5,25,17,15)),
                new Flight("A004", route, of(2016,5,25,20,45), of(2016,5,25,21,0))
            ));
        });
    }
}
