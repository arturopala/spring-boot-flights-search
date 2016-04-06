package me.arturopala.flights.utils;

import me.arturopala.flights.model.Airport;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class AirportFormatter implements Formatter<Airport> {

    @Override
    public Airport parse(String s, Locale locale) throws ParseException {
        return Airport.of(s);
    }

    @Override
    public String print(Airport airport, Locale locale) {
        return airport.getIataCode();
    }
}
