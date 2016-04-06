package me.arturopala.flights;

import me.arturopala.flights.dto.RouteDto;
import me.arturopala.flights.dto.SchedulesDto;
import me.arturopala.flights.integration.RoutesApiClient;
import me.arturopala.flights.integration.SchedulesApiClient;
import me.arturopala.flights.model.Airport;
import me.arturopala.flights.model.Route;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static me.arturopala.flights.Airports.*;

@SpringBootApplication
public class TestApplication extends Application {

    private static final List<RouteDto> PREDEFINED_ROUTES = Arrays.asList(
        RouteDto.of(WRO, DUB),
        RouteDto.of(DUB, WRO),
        RouteDto.of(WRO, STN),

        RouteDto.of(STN, WRO),
        RouteDto.of(STN, DUB),
        RouteDto.of(DUB, STN),
        RouteDto.of(VGO, DUB),
        RouteDto.of(DUB, VGO),
        RouteDto.of(VGO, STN),
        RouteDto.of(STN, VGO),
        RouteDto.of(ALC, VGO),
        RouteDto.of(VGO, ALC),
        RouteDto.of(SXF, ALC),
        RouteDto.of(ALC, SXF)
    );

    private static final Optional<SchedulesDto> PREDEFINED_SCHEDULES_WRO_DUB_2016_05 = Optional.of(
        new SchedulesDto(5, Arrays.asList(
            new SchedulesDto.Schedule( 1, Arrays.asList(
                new SchedulesDto.Connection("A001", "10:15","11:30"),
                new SchedulesDto.Connection("A002", "12:30","13:45"),
                new SchedulesDto.Connection("A003", "16:00","17:15"),
                new SchedulesDto.Connection("A004", "20:45","21:00")
            )),
            new SchedulesDto.Schedule( 3, Arrays.asList(
                new SchedulesDto.Connection("A001", "10:15","11:30"),
                new SchedulesDto.Connection("A002", "12:30","13:45")
            )),
            new SchedulesDto.Schedule( 7, Arrays.asList(
                new SchedulesDto.Connection("A001", "10:15","11:30"),
                new SchedulesDto.Connection("A003", "16:00","17:15"),
                new SchedulesDto.Connection("A004", "20:45","21:00")
            )),
            new SchedulesDto.Schedule( 13, Arrays.asList(
                new SchedulesDto.Connection("A004", "20:45","21:00")
            )),
            new SchedulesDto.Schedule( 21, Arrays.asList(
                new SchedulesDto.Connection("A003", "16:00","17:15"),
                new SchedulesDto.Connection("A004", "20:45","21:00")
            )),
            new SchedulesDto.Schedule( 25, Arrays.asList(
                new SchedulesDto.Connection("A003", "16:00","17:15"),
                new SchedulesDto.Connection("A004", "20:45","21:00")
            ))
        )
    ));

    private static final Optional<SchedulesDto> PREDEFINED_SCHEDULES_DUB_VGO_2016_05 = Optional.of(
        new SchedulesDto(5, Arrays.asList(
            new SchedulesDto.Schedule( 1, Arrays.asList(
                new SchedulesDto.Connection("B001", "07:00","09:00"),
                new SchedulesDto.Connection("B002", "19:00","21:00")
            )),
            new SchedulesDto.Schedule( 3, Arrays.asList(
                new SchedulesDto.Connection("B001", "07:00","09:00"),
                new SchedulesDto.Connection("B002", "19:00","21:00")
            )),
            new SchedulesDto.Schedule( 5, Arrays.asList(
                new SchedulesDto.Connection("B001", "07:00","09:00"),
                new SchedulesDto.Connection("B002", "19:00","21:00")
            )),
            new SchedulesDto.Schedule( 7, Arrays.asList(
                new SchedulesDto.Connection("B001", "07:00","09:00"),
                new SchedulesDto.Connection("B002", "19:00","21:00")
            )),
            new SchedulesDto.Schedule( 9, Arrays.asList(
                new SchedulesDto.Connection("B001", "07:00","09:00"),
                new SchedulesDto.Connection("B002", "19:00","21:00")
            )),
            new SchedulesDto.Schedule( 11, Arrays.asList(
                new SchedulesDto.Connection("B001", "07:00","09:00"),
                new SchedulesDto.Connection("B002", "19:00","21:00")
            )),
            new SchedulesDto.Schedule( 13, Arrays.asList(
                new SchedulesDto.Connection("B001", "07:00","09:00"),
                new SchedulesDto.Connection("B002", "19:00","21:00")
            ))
        )
    ));

    @Bean
    @Primary
    public RoutesApiClient routesApiClient(){
        return new RoutesApiClient(new RestTemplate(),""){
            @Override
            public List<RouteDto> getRoutes() {
                return PREDEFINED_ROUTES;
            }
        };
    }

    @Bean
    @Primary
    public SchedulesApiClient schedulesApiClient(){
        return new SchedulesApiClient(new RestTemplate(),""){
            @Override
            public Optional<SchedulesDto> getSchedules(Airport departure, Airport arrival, YearMonth yearMonth) {
                Route route = Route.of(departure,arrival);
                if(route == Route.of(WRO,DUB)) return PREDEFINED_SCHEDULES_WRO_DUB_2016_05;
                else if(route == Route.of(DUB,VGO)) return PREDEFINED_SCHEDULES_DUB_VGO_2016_05;
                else return Optional.empty();
            }
        };
    }
}
