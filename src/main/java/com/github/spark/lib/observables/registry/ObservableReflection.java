package com.github.spark.lib.observables.registry;

import com.github.spark.lib.SparkContext;
import java.util.logging.Level;
import com.github.spark.lib.framework.Framework;
import com.github.spark.lib.observables.annotations.ObserveMutation;
import com.github.spark.lib.observables.dto.MutationEventObserver;
import com.github.spark.lib.services.annotations.Service;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

public final class ObservableReflection {
    public static ArrayList<MutationEventObserver> findObserverCallbacks(Framework framework) {
        ArrayList<MutationEventObserver> callbacksToRegister = new ArrayList<>();
        Reflections reflections = new Reflections(SparkContext.basePackage, Scanners.MethodsAnnotated);

        Set<Method> callbackMethods = reflections.getMethodsAnnotatedWith(ObserveMutation.class);
        for (Method callbackMethod : callbackMethods) {
            ObserveMutation observeMutationAnnotation = callbackMethod.getAnnotation(ObserveMutation.class);

            Class<?> methodClass = callbackMethod.getDeclaringClass();
            Service serviceAnnotation = methodClass.getAnnotation(Service.class);

            if (serviceAnnotation != null) {
                Object serviceInstance = framework.serviceRegistry.getItem(methodClass);
                callbackMethod.setAccessible(true);
                callbacksToRegister.add(new MutationEventObserver(observeMutationAnnotation.type(), serviceInstance, callbackMethod));
            } else {
                framework.log(Level.SEVERE, "Cannot register @ObserveMutation in " + methodClass.getSimpleName() + " since it is not a service");
            }
        }

        return callbacksToRegister;
    }
}


