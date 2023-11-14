package com.github.spark.lib;

import com.github.spark.lib.datastores.DataStore;
import com.github.spark.lib.datastores.DataStoreItem;
import com.github.spark.lib.datastores.DataStoreReflection;
import com.github.spark.lib.events.EventReflection;
import com.github.spark.lib.events.PlayerCommand;
import com.github.spark.lib.framework.Framework;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.logging.Level;

public abstract class SparkPlugin extends JavaPlugin {
    public Framework framework;

    /**
     * This is called after all commands and listeners have been registered
     */
    public abstract void onFrameworkEnable();

    /**
     * This is called before spark does anything
     * Used for setting spark context properties
     */
    public void onBeforeFrameworkInitialize() {

    }

    @Override
    public final void onEnable() {
        onBeforeFrameworkInitialize();

        getLogger().log(Level.INFO, "[SPARK] Initializing Framework...");

        if (!getDataFolder().exists()) {
            getLogger().log(Level.INFO, "[SPARK] Data folder not found creating...");
            getDataFolder().mkdir();
        }

        this.framework = new Framework(this);

        framework.log(Level.INFO, "Registering services...");
        onRegisterServices();
        framework.log(Level.INFO, "Registering plugin datastores...", true);
        onRegisterDataStores();

        this.framework.createInjector();

        onRegisterListeners();
        onInjectAllMembers();
        onRegisterInternalCommands();
        onRegisterCommands();

        this.onFrameworkEnable();
    }

    @Override
    public void onDisable() {
        framework.saveDataStores();
    }

    private void onRegisterInternalCommands() {
        PlayerCommand playerCommand = new PlayerCommand();
        framework.injectMembers(playerCommand);
        framework.addListener(playerCommand);
    }

    private void onRegisterDataStores() {
        framework.dataStoreRegistry.findAndRegisterDataStores();
    }

    private void onRegisterServices() {
        framework.serviceRegistry.findAndRegisterServices();
    }

    private void onRegisterListeners() {
        framework.eventRegistry.findAndRegisterEventHandlers(framework);
    }

    private void onRegisterCommands() {
        framework.commandRegistry.findAndRegisterCommands();
    }

    private void onInjectAllMembers() {
        framework.dataStoreRegistry.injectMembers();
        framework.serviceRegistry.injectMembers();
        framework.commandRegistry.injectMembers();
        framework.eventRegistry.injectMembers();
    }
}
