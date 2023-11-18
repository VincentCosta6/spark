package com.github.spark.lib;

import com.github.spark.lib.events.PlayerCommand;
import com.github.spark.lib.framework.Framework;
import com.github.spark.lib.services.custom.MetadataService;
import com.github.spark.lib.services.custom.ObserverService;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

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

    public void onBeforeFrameworkRegistrations() {}

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
        onBeforeFrameworkRegistrations();
        onRegisterServices();
        framework.log(Level.INFO, "Registering plugin datastores...", true);
        onRegisterDataStores();

        this.framework.createInjector();

        onRegisterListeners();
        onRegisterInternalCommands();
        onRegisterCommands();
        onInjectAllMembers();

        // observable callbacks do not need to be injected
        onRegisterObservableCallbacks();

        this.onFrameworkEnable();

        if (SparkContext.saveIntervalSeconds != null && SparkContext.saveIntervalSeconds > 0) {
            framework.log(Level.INFO, "Found saveIntervalSeconds setting, creating save interval", true);

            new BukkitRunnable(){
                long lastRun = 0;
                @Override
                public void run(){
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastRun > SparkContext.saveIntervalSeconds * 1000) {
                        lastRun = currentTime;
                        framework.log("saving datastores...");
                        framework.saveDataStores();
                    }
                }
            }.runTaskTimer(framework.plugin, 0L, 20L);
        }
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
        framework.dataStoreRegistry.findAndRegisterItems();
    }

    private void onRegisterServices() {
        framework.serviceRegistry.addItem(MetadataService.class, new MetadataService());
        framework.serviceRegistry.addItem(ObserverService.class, new ObserverService());
        framework.serviceRegistry.findAndRegisterItems();
    }

    private void onRegisterListeners() {
        framework.eventRegistry.findAndRegisterItems();
    }

    private void onRegisterCommands() {
        framework.commandRegistry.findAndRegisterItems();
    }

    private void onRegisterObservableCallbacks() {
        framework.observableRegistry.findAndRegisterItems();
    }

    private void onInjectAllMembers() {
        framework.dataStoreRegistry.injectMembers();
        framework.serviceRegistry.injectMembers();
        framework.commandRegistry.injectMembers();
        framework.eventRegistry.injectMembers();
    }
}
