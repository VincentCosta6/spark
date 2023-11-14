package com.github.spark.lib.injectable;

import com.github.spark.lib.framework.Framework;
import com.github.spark.lib.framework.FrameworkInjectable;

import java.util.HashMap;

public class CustomInjectableRegistry implements FrameworkInjectable {
    private final Framework framework;
    private final HashMap<Class<CustomInjectable>, CustomInjectable> customInjectables = new HashMap<>();

    public CustomInjectableRegistry(Framework framework) {
        this.framework = framework;
    }


    @Override
    public boolean injectMembers() {
        return false;
    }
}
