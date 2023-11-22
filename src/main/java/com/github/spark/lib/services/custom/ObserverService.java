package com.github.spark.lib.services.custom;

import com.github.spark.lib.framework.Framework;
import com.github.spark.lib.services.annotations.Service;
import com.google.inject.Inject;

@Service
public class ObserverService {
    @Inject Framework framework;

    public <T> void notifyObserverOfMutation(Class<?> clazz, T item) {
        framework.observableRegistry.notifyObservers(clazz, item);
    }
}
