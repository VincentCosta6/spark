package com.github.spark.examples;

import com.github.spark.examples.events.PlayerMove;
import com.github.spark.lib.SparkContext;
import com.github.spark.lib.SparkPlugin;

public final class PluginEntry extends SparkPlugin {
    @Override
    public void onBeforeFrameworkInitialize() {
        SparkContext.setBasePackage("com.github.spark.examples");
    }

    @Override
    public void onFrameworkEnable() {
        this.framework.log("Started");
    }
}
