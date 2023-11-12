package com.github.spark.lib.events;

import com.github.spark.lib.Framework;
import com.github.spark.lib.SparkContext;
import org.bukkit.event.Listener;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.Set;

public class EventReflection {
    public static void findAndRegisterEvents(Framework framework) {
        Reflections reflections = new Reflections(SparkContext.basePackage, Scanners.TypesAnnotated);

        Set<Class<?>> registerEventsClasses = reflections.getTypesAnnotatedWith(RegisterEvents.class);
        for (Class<?> registerEventsClass : registerEventsClasses) {
            if (registerEventsClass.getEnclosingClass() == null && Listener.class.isAssignableFrom(registerEventsClass)) {
                try {
                    Listener listener = (Listener) registerEventsClass.getDeclaredConstructor().newInstance();
                    framework.addListener(listener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
