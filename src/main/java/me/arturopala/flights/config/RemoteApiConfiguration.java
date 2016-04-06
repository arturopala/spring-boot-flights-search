package me.arturopala.flights.config;

import me.arturopala.flights.integration.RoutesApiClient;
import me.arturopala.flights.integration.SchedulesApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RemoteApiConfiguration {

    @Bean
    public RoutesApiClient routesApiClient(@Value("${routesApi.url}") final String baseUrl) {
        return new RoutesApiClient(new RestTemplate(), baseUrl);
    }

    @Bean
    public SchedulesApiClient schedulesApiClient(@Value("${schedulesApi.url}") final String baseUrl) {
        return new SchedulesApiClient(new RestTemplate(), baseUrl);
    }
}
