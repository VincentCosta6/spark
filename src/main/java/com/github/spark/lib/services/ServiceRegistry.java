package com.github.spark.lib.services;

import com.github.spark.lib.framework.Framework;
import com.github.spark.lib.framework.FrameworkInjectable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Supplier;

public class ServiceRegistry implements FrameworkInjectable {
    private final Framework framework;
    private HashMap<Class<?>, Object> services = new HashMap<>();
    private HashMap<Class<?>, Supplier<?>> singletonServiceFactories = new HashMap<>();

    public ServiceRegistry(Framework framework) {
        this.framework = framework;
    }

    public boolean findAndRegisterServices() {
        ArrayList<Object> foundServices = ServiceReflection.findServices(singletonServiceFactories);

        for(Object service : foundServices) {
            services.put(service.getClass(), service);
        }

        return true;
    }

    public <T> void registerSingletonServiceFactory(Class<T> clazz, Supplier<T> singletonFactory) {
        this.singletonServiceFactories.put(clazz, singletonFactory);
    }

    public Iterator<Object> getEntries() {
        return services.values().iterator();
    }

    public boolean injectMembers() {
        for(Object service : services.values()) {
            framework.injectMembers(service);
        }

        return true;
    }
}
