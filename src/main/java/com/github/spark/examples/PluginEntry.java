package com.github.spark.examples;

import com.github.spark.examples.services.CustomService;
import com.github.spark.examples.services.RunnableService;
import com.github.spark.lib.common.SparkContext;
import com.github.spark.lib.SparkPlugin;

public final class PluginEntry extends SparkPlugin {
    @Override
    public void onBeforeFrameworkInitialize() {
        SparkContext.setBasePackage("com.github.spark.examples");
        SparkContext.setSaveInterval(5);
    }

    public void onBeforeFrameworkRegistrations() {
        framework.registerSingletonServiceFactory(CustomService.class, () -> new CustomService(6));
    }

    @Override
    public void onFrameworkEnable() {
        RunnableService service = (RunnableService) framework.serviceRegistry.get(RunnableService.class);

        service.createRunnable();
    }
}
