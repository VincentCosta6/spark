package com.github.spark.lib.observables.dto;

import java.lang.reflect.Method;

public record MutationEventObserver(Class<?> clazz, Object instance, Method method) {
}
