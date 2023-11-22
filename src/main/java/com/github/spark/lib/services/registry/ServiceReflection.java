package com.github.spark.lib.services.registry;

import com.github.spark.lib.common.SparkContext;
import com.github.spark.lib.services.annotations.Service;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Supplier;

public class ServiceReflection {
    public static ArrayList<Object> findServices(HashMap<Class<?>, Supplier<?>> singletonFactories) {
        ArrayList<Object> services = new ArrayList<>();
        Reflections reflections = new Reflections(SparkContext.basePackage, Scanners.TypesAnnotated);

        Set<Class<?>> serviceClasses = reflections.getTypesAnnotatedWith(Service.class);
        for (Class<?> serviceClazz : serviceClasses) {
            try {
                Supplier<?> singletonFactory = singletonFactories.get(serviceClazz);

                Object serviceObject;
                if (singletonFactory != null) {
                    serviceObject = singletonFactory.get();
                } else {
                    serviceObject = serviceClazz.getDeclaredConstructor().newInstance();
                }

                services.add(serviceObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return services;
    }
}
