package me.arturopala.flights.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.validation.ValidationException;
import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class Airport implements Serializable {

    private final String iataCode;

    private Airport(String iataCode) {
        this.iataCode = iataCode;
    }

    @JsonValue
    public String getIataCode() {
        return iataCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport = (Airport) o;
        return Objects.equals(iataCode, airport.iataCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iataCode);
    }

    @Override
    public String toString() {
        return iataCode;
    }

    // FACTORY

    private static final ConcurrentHashMap<String,Airport> AIRPORTS = new ConcurrentHashMap<>();

    @JsonCreator
    public static Airport of(String iataCode) {
        if(iataCode==null || iataCode.isEmpty() || iataCode.length()>3){
            throw new ValidationException("Invalid IATA code format: "+iataCode);
        }
        return AIRPORTS.computeIfAbsent(iataCode, code -> new Airport(code));
    }
}
