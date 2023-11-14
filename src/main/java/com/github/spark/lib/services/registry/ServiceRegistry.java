package com.github.spark.lib.services.registry;

import com.github.spark.lib.common.Registry;
import com.github.spark.lib.framework.Framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

public class ServiceRegistry extends Registry<Class<?>, Object> {
    private final HashMap<Class<?>, Supplier<?>> singletonServiceFactories = new HashMap<>();

    public ServiceRegistry(Framework framework) {
        super(framework);
    }

    public <T> void registerSingletonServiceFactory(Class<T> clazz, Supplier<T> singletonFactory) {
        this.singletonServiceFactories.put(clazz, singletonFactory);
    }

    @Override
    public void findAndRegisterItems() {
        ArrayList<Object> foundServices = ServiceReflection.findServices(singletonServiceFactories);

        for(Object service : foundServices) {
            items.put(service.getClass(), service);
        }
    }
}
