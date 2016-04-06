package me.arturopala.flights.service;

import me.arturopala.flights.TestApplication;
import me.arturopala.flights.UnitTest;
import me.arturopala.flights.dto.FlightDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import static me.arturopala.flights.Airports.DUB;
import static me.arturopala.flights.Airports.VGO;
import static me.arturopala.flights.Airports.WRO;
import static me.arturopala.flights.service.impl.FlightsServiceImpl.getMonthsBetween;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;

@SpringApplicationConfiguration(TestApplication.class)
public class FlightsServiceTest extends UnitTest {

    @Autowired
    private FlightsService flightsService;

    @Test
    public void shouldFindFlightsForSingleLegRoute() throws Exception {
        List<FlightDto> flights = flightsService.findFlights(WRO, DUB, LocalDateTime.of(2016, 5, 2, 11, 20), LocalDateTime.of(2016, 5, 5, 17, 17), 1, 120, 1440);
        assertThat(flights, hasSize(2));
    }

    @Test
    public void shouldFindFlightsForMultiLegRoute() throws Exception {
        List<FlightDto> flights = flightsService.findFlights(WRO, VGO, LocalDateTime.of(2016, 5, 2, 11, 20), LocalDateTime.of(2016, 5, 5, 17, 17), 1, 120, 1440);
        assertThat(flights, hasSize(2));
    }

    @Test
    public void shouldCalculateMonthsBetweenDatesWhenSameYear() throws Exception {
        LocalDateTime start = LocalDateTime.of(2016, 1, 12, 15, 13);
        LocalDateTime end = LocalDateTime.of(2016, 5, 31, 23, 59, 59);
        assertThat(getMonthsBetween(start, end), contains(YearMonth.of(2016,1),YearMonth.of(2016,2),YearMonth.of(2016,3),YearMonth.of(2016,4),YearMonth.of(2016,5)));
    }

    @Test
    public void shouldCalculateMonthsBetweenDatesWhenDifferentYears() throws Exception {
        LocalDateTime start = LocalDateTime.of(2015, 11, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2016, 3, 1, 0, 0, 0);
        assertThat(getMonthsBetween(start, end), contains(YearMonth.of(2015,11),YearMonth.of(2015,12),YearMonth.of(2016,1),YearMonth.of(2016,2),YearMonth.of(2016,3)));
    }

    @Test
    public void shouldCalculateMonthsBetweenDatesWhenSameMonth() throws Exception {
        LocalDateTime start = LocalDateTime.of(2016, 3, 11, 12, 54, 21);
        LocalDateTime end = LocalDateTime.of(2016, 3, 15, 6, 7, 35);
        assertThat(getMonthsBetween(start, end), contains(YearMonth.of(2016,3)));
    }

    @Test
    public void shouldCalculateMonthsBetweenDatesWhenEndBeforeStart() throws Exception {
        LocalDateTime start = LocalDateTime.of(2016, 5, 12, 15, 13);
        LocalDateTime end = LocalDateTime.of(2016, 1, 31, 23, 59, 59);
        assertThat(getMonthsBetween(start, end), contains(YearMonth.of(2016,5),YearMonth.of(2016,4),YearMonth.of(2016,3),YearMonth.of(2016,2),YearMonth.of(2016,1)));
    }

}
