package com.github.spark.lib;

import com.github.spark.lib.datastores.DataStore;
import com.github.spark.lib.datastores.DataStoreItem;
import com.github.spark.lib.datastores.DataStoreReflection;
import com.github.spark.lib.events.EventReflection;
import com.github.spark.lib.events.PlayerCommand;
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

        /////////// LOAD PLUGIN STORES ///////////////////
        framework.log(Level.INFO, "Registering plugin datastores...", true);
        onLoadDataStores();
        this.framework.handleStoresLoaded();

        this.framework.loadCommands();
        onLoadInternalCommands();

        onRegisterListeners();

        this.onFrameworkEnable();
    }

    @Override
    public void onDisable() {
        framework.saveDataStores();
    }

    private void onLoadInternalCommands() {
        framework.addListener(new PlayerCommand());
    }

    private void onLoadDataStores() {
        ArrayList<DataStore<? extends DataStoreItem>> dataStores = DataStoreReflection.findDataStores();
        dataStores.forEach(dataStore -> framework.addDataStore(dataStore));
    }

    private void onRegisterListeners() {
        EventReflection.findAndRegisterEvents(framework);
    }
}
