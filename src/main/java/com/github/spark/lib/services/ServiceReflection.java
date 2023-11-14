package com.github.spark.lib.services;

import com.github.spark.lib.SparkContext;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.ArrayList;
import java.util.Set;

public class ServiceReflection {
    public static ArrayList<Object> findServices() {
        ArrayList<Object> services = new ArrayList<>();
        Reflections reflections = new Reflections(SparkContext.basePackage, Scanners.TypesAnnotated);

        Set<Class<?>> serviceClasses = reflections.getTypesAnnotatedWith(Service.class);
        for (Class<?> serviceClazz : serviceClasses) {
            try {
                services.add(serviceClazz.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return services;
    }
}
