package me.arturopala.flights.integration;

import me.arturopala.flights.dto.SchedulesDto;
import me.arturopala.flights.model.Airport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.YearMonth;
import java.util.Optional;

import static javafx.scene.input.KeyCode.L;

/**
 * Schedules API client.
 *
 * Calls https://api.ryanair.com/timetable/3/schedules/{departure}/{arrival}/years/{year}/months/{month}
 * which returns a list of available flights for a given:
 * <li>departure airport IATA code,
 * <li>an arrival airport IATA code,
 * <li>a year and
 * <li>a month.
 *
 * For example (https://api.ryanair.com/timetable/3/schedules/DUB/WRO/years/2016/months/6):
 * <pre>
 * {
 *   "month": 6, # a month of a year
 *   "days": [{
 *     "day": 1, # a day of a month
 *     "flights": [ # a list of flights for given day {
 *        "number": "1926", # a flight number
 *        "departureTime": "17:50", # a departure time in the departure airport timezone
 *        "arrivalTime": "21:25" # an arrival time in the arrival airport timezone
 *      }
 *    ]
 *  }]
 * }
 * </pre>
 */
public class SchedulesApiClient extends BaseClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulesApiClient.class);

    public SchedulesApiClient(RestTemplate template, String baseUrl) {
        this.template = template;
        this.baseUrl = baseUrl;
    }

    public Optional<SchedulesDto> getSchedules(Airport departure, Airport arrival, YearMonth yearMonth) {
        try {
            ParameterizedTypeReference<SchedulesDto> type = new ParameterizedTypeReference<SchedulesDto>() {};
            LOGGER.info("GET schedules from Schedules API ("+departure+"->"+arrival+","+yearMonth+")");
            ResponseEntity<SchedulesDto> instance = template.exchange(baseUrl, HttpMethod.GET, null, type, departure.getIataCode(), arrival.getIataCode(), yearMonth.getYear(), yearMonth.getMonthValue());
            if(instance.getStatusCode()== HttpStatus.OK) {
                return Optional.of(instance.getBody());
            }
            else return Optional.empty();
        }
        catch (Exception e) {
            LOGGER.error("Unable to get schedules from Schedules API for ("+departure+"->"+arrival+","+yearMonth+")");
            return Optional.empty();
        }
    }
}
