package com.github.spark.examples;

import com.github.spark.examples.datastores.PlayerStateDataStore;
import com.github.spark.examples.events.PlayerMove;
import com.github.spark.lib.SparkPlugin;
import com.github.spark.examples.commands.TestCommand;

public final class PluginEntry extends SparkPlugin {
    @Override
    public void onFrameworkEnable() {
        this.framework.log("Started");
    }

    @Override
    public void registerDataStores() {
        framework.addDataStore(new PlayerStateDataStore());
    }

    @Override
    public void registerCommands() {
        framework.addCommand(new TestCommand());
    }

    @Override
    public void registerListeners() {
        framework.addListener(new PlayerMove());
    }
}
