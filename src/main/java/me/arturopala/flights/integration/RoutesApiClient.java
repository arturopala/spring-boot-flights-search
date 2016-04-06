package me.arturopala.flights.integration;

import me.arturopala.flights.dto.RouteDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Routes API client.
 *
 * Calls https://api.ryanair.com/core/3/routes/
 * which returns a list of all available routes based on theairport's IATA codes.
 *
 * For example:
 * [{
 *   "airportFrom":"LUZ",
 *   "airportTo":"STN",
 *   "newRoute":false,
 *   "seasonalRoute":false
 * }]
 */
public class RoutesApiClient extends BaseClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoutesApiClient.class);

    public RoutesApiClient(RestTemplate template, String baseUrl) {
        this.template = template;
        this.baseUrl = baseUrl;
    }

    public List<RouteDto> getRoutes() {
        try {
            ParameterizedTypeReference<List<RouteDto>> type = new ParameterizedTypeReference<List<RouteDto>>() {};
            ResponseEntity<List<RouteDto>> instance = template.exchange(baseUrl, HttpMethod.GET, null, type);
            LOGGER.info("Got response from Routes API "+instance.getStatusCode());
            if(instance.getStatusCode()== HttpStatus.OK)
                return instance.getBody();
            else return Collections.emptyList();
        } catch (Exception e) {
            LOGGER.error("Unable to get routes from Routes API", e);
            return Collections.emptyList();
        }
    }
}
