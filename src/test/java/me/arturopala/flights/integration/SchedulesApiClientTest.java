package me.arturopala.flights.integration;

import me.arturopala.flights.UnitTest;
import me.arturopala.flights.dto.SchedulesDto;
import me.arturopala.flights.model.Airport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SchedulesApiClientTest extends UnitTest {

    @Autowired
    private SchedulesApiClient client;

    @Test
    public void shouldGetFlightsFromWROToDUBInCurrentMonth() {
        Optional<SchedulesDto> flights = client.getSchedules(
            Airport.of("WRO"), Airport.of("DUB"), YearMonth.now());
        assertThat(flights, not(Optional.empty()));
        flights.ifPresent(f -> {
            assertThat(f.getMonth(), is(LocalDate.now().getMonthValue()));
            assertThat(f.getDays(), not(nullValue()));
            assertThat(f.getDays().size(),greaterThan(0));
            f.getDays().forEach(d -> {
                assertThat(d.getDay(),greaterThan(0));
                assertThat(d.getDay(),lessThanOrEqualTo(LocalDate.now().getMonth().maxLength()));
                assertThat(d.getFlights(), not(nullValue()));
                assertThat(d.getFlights().size(), greaterThan(0));
                d.getFlights().forEach(flight -> {
                    assertThat(flight.getNumber(), not(isEmptyOrNullString()));
                    assertThat(flight.getArrivalTime(), not(nullValue()));
                    assertThat(flight.getDepartureTime(), not(nullValue()));
                });
            });
        });
        if(!flights.isPresent()) fail("Missing schedules.");
    }

}
