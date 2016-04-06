package me.arturopala.flights.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import me.arturopala.flights.utils.LocalIsoDateTimeSerializer;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;

@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY, fieldVisibility = JsonAutoDetect.Visibility.NONE)
@JsonPropertyOrder({"departureAirport","arrivalAirport","departureDateTime","arrivalDateTime"})
public class Flight implements Serializable {

    private final String number;
    private final Route route;
    private final LocalDateTime departureTime;
    private final LocalDateTime arrivalTime;


    public Flight(String number, Route route, LocalDateTime departureTime, LocalDateTime arrivalTime) {
        this.number = number;
        this.route = route;
        this.departureTime =  departureTime;
        this.arrivalTime = arrivalTime;
    }

    @JsonIgnore
    public Route getRoute() {
        return route;
    }

    @JsonGetter("departureAirport")
    public Airport getDepartureAirport(){
        return route.getFrom();
    }

    @JsonGetter("arrivalAirport")
    public Airport getArrivalAirport(){
        return route.getTo();
    }

    @JsonGetter("departureDateTime")
    @JsonSerialize(using = LocalIsoDateTimeSerializer.class)
    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    @JsonGetter("arrivalDateTime")
    @JsonSerialize(using = LocalIsoDateTimeSerializer.class)
    public LocalDateTime getArrivalTime(){
        return arrivalTime;
    }

    @JsonIgnore
    public String getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return Objects.equals(number, flight.number) &&
            Objects.equals(route, flight.route) &&
            Objects.equals(departureTime, flight.departureTime) &&
            Objects.equals(arrivalTime, flight.arrivalTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, route, departureTime, arrivalTime);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Flight{");
        sb.append("number='").append(number).append('\'');
        sb.append(", route=").append(route);
        sb.append(", departureTime=").append(departureTime);
        sb.append(", arrivalTime=").append(arrivalTime);
        sb.append('}');
        return sb.toString();
    }

    // STATIC

    public static final Comparator<? super Flight> FLIGHT_DEPARTURE_TIME_COMPARATOR =
        (Comparator<Flight>) (f1, f2) -> f1.getDepartureTime().compareTo(f2.getDepartureTime());
}
