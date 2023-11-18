package com.github.spark.lib.observables;

public class MutationEvent<T> {
    private final T eventData;

    public MutationEvent(T eventData) {
        this.eventData = eventData;
    }

    public T getEventData() {
        return eventData;
    }
}