package me.arturopala.flights.controller;

import me.arturopala.flights.ApiTest;
import me.arturopala.flights.Constants;
import me.arturopala.flights.TestApplication;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringApplicationConfiguration(TestApplication.class)
public class FlightsControllerTest extends ApiTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightsControllerTest.class);

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    private static final String INTERCONNECTIONS_URI = Constants.URI_SEARCH_INTERCONNECTIONS + "?departure={departure}&arrival={arrival}&" +
        "departureDateTime={departureDateTime}&arrivalDateTime={arrivalDateTime}";

    @Test
    public void shouldGetMultiLegFlights() throws Exception {
        //given
        String departureDateTime = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.of(2016, 5, 2, 11, 20));
        String arrivalDateTime = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.of(2016, 5, 5, 17, 17));
        MockHttpServletRequestBuilder requestBuilder = get(INTERCONNECTIONS_URI, "WRO", "VGO", departureDateTime, arrivalDateTime);
        //when
        ResultActions perform = mvc.perform(requestBuilder);
        //then
        perform.andExpect(status().isOk())
            .andDo(result -> LOGGER.info(result.getResponse().getContentAsString()))
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.length()", is(2)))
            .andExpect(jsonPath("$[0].stops", is(1)))
            .andExpect(jsonPath("$[0].legs.length()", is(2)))
            .andExpect(jsonPath("$[0].legs[0].departureAirport", is("WRO")))
            .andExpect(jsonPath("$[0].legs[0].arrivalAirport", is("DUB")))
            .andExpect(jsonPath("$[0].legs[0].departureDateTime", is("2016-05-03T10:15")))
            .andExpect(jsonPath("$[0].legs[0].arrivalDateTime", is("2016-05-03T11:30")))
            .andExpect(jsonPath("$[1].stops", is(1)))
            .andExpect(jsonPath("$[1].legs.length()", is(2)))
        ;
    }

    @Test
    public void shouldGetEmptyFlightsWhenDepartureEqualsArrival() throws Exception {
        //given
        String departureDateTime = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now().plusWeeks(1));
        String arrivalDateTime = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now().plusWeeks(1).plusDays(4));
        MockHttpServletRequestBuilder requestBuilder = get(INTERCONNECTIONS_URI, "WRO", "WRO", departureDateTime, arrivalDateTime);
        //when
        ResultActions perform = mvc.perform(requestBuilder);
        //then
        perform.andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(content().string("[]"))
        ;
    }

    @Test
    public void shouldGetEmptyFlightsWhenDepartureAfterArrival() throws Exception {
        //given
        String departureDateTime = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now().plusWeeks(1));
        String arrivalDateTime = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now().plusWeeks(1).minusMinutes(1));
        MockHttpServletRequestBuilder requestBuilder = get(INTERCONNECTIONS_URI, "WRO", "DUB", departureDateTime, arrivalDateTime);
        //when
        ResultActions perform = mvc.perform(requestBuilder);
        //then
        perform.andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(content().string("[]"))
        ;
    }
}
