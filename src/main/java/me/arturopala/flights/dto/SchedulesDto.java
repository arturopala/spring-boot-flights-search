package me.arturopala.flights.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Collection;

public class SchedulesDto implements Serializable {

    private final int month;
    private final Collection<Schedule> days;

    @JsonCreator
    public SchedulesDto(
        @JsonProperty("month") int month,
        @JsonProperty("days") Collection<Schedule> days) {
            this.days = days;
            this.month = month;
    }

    public Collection<Schedule> getDays() {
        return days;
    }

    public int getMonth() {
        return month;
    }

    public static class Schedule implements Serializable {

        private final int day;
        private final Collection<Connection> flights;

        @JsonCreator
        public Schedule(
            @JsonProperty("day") int day,
            @JsonProperty("flights") Collection<Connection> flights) {
                this.day = day;
                this.flights = flights;
        }

        public int getDay() {
            return day;
        }

        public Collection<Connection> getFlights() {
            return flights;
        }
    }

    public static class Connection implements Serializable {

        private final String number;
        private final LocalTime departureTime;
        private final LocalTime arrivalTime;

        @JsonCreator
        public Connection(@JsonProperty("number") String number, @JsonProperty("departureTime") String departureTime, @JsonProperty("arrivalTime") String arrivalTime) {
            this.number = number;
            this.departureTime =  LocalTime.parse(departureTime);;
            this.arrivalTime =  LocalTime.parse(arrivalTime);;
        }

        public LocalTime getArrivalTime(){
            return arrivalTime;
        }

        public LocalTime getDepartureTime() {
            return departureTime;
        }

        public String getNumber() {
            return number;
        }
    }
}
