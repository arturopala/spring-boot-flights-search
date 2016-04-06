package me.arturopala.flights.model;

import javax.validation.ValidationException;
import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class Route implements Serializable {

    private final Airport from;
    private final Airport to;

    private Route(Airport from, Airport to) {
        this.from = from;
        this.to = to;
    }

    public Airport getFrom() {
        return from;
    }

    public Airport getTo() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return Objects.equals(from, route.from) &&
            Objects.equals(to, route.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    @Override
    public String toString() {
        return keyOf(from,to);
    }

    // FACTORY

    private static final String keyOf(Airport from, Airport to){
        final StringBuilder sb = new StringBuilder("(");
        sb.append(from);
        sb.append("->").append(to);
        sb.append(')');
        return sb.toString();
    }

    private static final ConcurrentHashMap<String,Route> ROUTES = new ConcurrentHashMap<>();

    public static Route of(Airport from, Airport to){
        if(from==null || to==null){
            throw new ValidationException("Route from and to airports MUST NOT be null");
        }
        if(from.equals(to)){
            throw new ValidationException("Route from and to airports MUST be different");
        }
        return ROUTES.computeIfAbsent(keyOf(from,to), k -> new Route(from, to));
    }
}
