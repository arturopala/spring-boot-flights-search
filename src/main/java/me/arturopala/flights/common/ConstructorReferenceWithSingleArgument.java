package me.arturopala.flights.common;

public interface ConstructorReferenceWithSingleArgument<T, A> {
    T createWithArgument(A arg);
}
