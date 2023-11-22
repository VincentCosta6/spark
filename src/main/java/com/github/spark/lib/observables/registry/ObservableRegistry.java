package com.github.spark.lib.observables.registry;

import com.github.spark.lib.common.Registry;
import com.github.spark.lib.framework.Framework;
import com.github.spark.lib.observables.dto.MutationEventObserver;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class ObservableRegistry extends Registry<Class<?>, ArrayList<MutationEventObserver>> {
    public ObservableRegistry(Framework framework) {
        super(framework);
    }

    @Override
    public void findAndRegisterItems() {
        ArrayList<MutationEventObserver> observers = ObservableReflection.findObserverCallbacks(framework);

        for(MutationEventObserver observer : observers) {
            ArrayList<MutationEventObserver> classObservers = items.get(observer.clazz());
            if (classObservers == null) {
                classObservers = new ArrayList<>();
                classObservers.add(observer);
                items.put(observer.clazz(), classObservers);
            } else {
                classObservers.add(observer);
            }
        }
    }

    public <T> void notifyObservers(Class<?> clazz, T item, T oldState) {
        ArrayList<MutationEventObserver> observers = items.get(clazz);

        if (observers == null || observers.size() == 0) {
            return;
        }

        observers.forEach(observer -> {
            try {
                observer.method().invoke(observer.instance(), item, oldState);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
