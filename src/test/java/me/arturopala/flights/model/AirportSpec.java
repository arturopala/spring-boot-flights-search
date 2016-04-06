package me.arturopala.flights.model;

import me.arturopala.flights.UnitTest;
import org.junit.Test;

import javax.validation.ValidationException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

public class AirportSpec extends UnitTest {

    @Test
    public void shouldParseIATACode() throws Exception {
        assertThat(Airport.of("WRO").getIataCode(), is("WRO"));
        assertThat(Airport.of("DUB").getIataCode(), is("DUB"));
    }

    @Test
    public void shouldCachedValues() throws Exception {
        assertThat(Airport.of("WRO"), sameInstance(Airport.of("WRO")));
    }

    @Test(expected = ValidationException.class)
    public void shouldNotParseIATACodeWhenNull() throws Exception {
        Airport.of(null).getIataCode();
    }

    @Test(expected = ValidationException.class)
    public void shouldNotParseIATACodeWhenEmpty() throws Exception {
        Airport.of("").getIataCode();
    }

    @Test(expected = ValidationException.class)
    public void shouldNotParseIATACodeWhenLongerThan3Chars() throws Exception {
        Airport.of("WROC").getIataCode();
    }
}
