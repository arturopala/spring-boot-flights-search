package me.arturopala.flights.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import me.arturopala.flights.model.Flight;

import javax.validation.ValidationException;
import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;

public class FlightDto implements Serializable {

    private final int stops;
    private final Collection<Flight> legs;

    @JsonCreator
    public FlightDto(
        @JsonProperty("stops") int stops,
        @JsonProperty("legs") Collection<Flight> legs) {
            if(legs==null){
                throw new ValidationException("Flights collection MAY NOT null");
            }
            this.legs = legs.stream().collect(Collectors.toList());
            this.stops = stops;
    }

    public Collection<Flight> getLegs() {
        return legs;
    }

    public int getStops() {
        return stops;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FlightDto{");
        sb.append("stops=").append(stops);
        sb.append(", legs=").append(legs);
        sb.append('}');
        return sb.toString();
    }
}
