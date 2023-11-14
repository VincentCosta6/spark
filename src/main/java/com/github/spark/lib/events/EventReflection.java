package com.github.spark.lib.events;

import com.github.spark.lib.SparkContext;
import org.bukkit.event.Listener;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.ArrayList;
import java.util.Set;

public class EventReflection {
    public static ArrayList<Listener> findEventHandlers() {
        ArrayList<Listener> eventHandlers = new ArrayList<>();
        Reflections reflections = new Reflections(SparkContext.basePackage, Scanners.TypesAnnotated);

        Set<Class<?>> registerEventsClasses = reflections.getTypesAnnotatedWith(RegisterEvents.class);
        for (Class<?> registerEventsClass : registerEventsClasses) {
            if (registerEventsClass.getEnclosingClass() == null && Listener.class.isAssignableFrom(registerEventsClass)) {
                try {
                    Listener listener = (Listener) registerEventsClass.getDeclaredConstructor().newInstance();
                    eventHandlers.add(listener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return eventHandlers;
    }
}
