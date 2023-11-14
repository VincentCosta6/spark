package com.github.spark.lib.services;

import com.github.spark.lib.datastores.DataStore;
import com.github.spark.lib.datastores.DataStoreItem;
import com.github.spark.lib.framework.Framework;
import com.github.spark.lib.framework.FrameworkInjectable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ServiceRegistry implements FrameworkInjectable {
    private final Framework framework;
    private HashMap<Class<?>, Object> services = new HashMap<>();

    public ServiceRegistry(Framework framework) {
        this.framework = framework;
    }

    public boolean findAndRegisterServices() {
        ArrayList<Object> foundServices = ServiceReflection.findServices();

        for(Object service : foundServices) {
            services.put(service.getClass(), service);
        }

        return true;
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
