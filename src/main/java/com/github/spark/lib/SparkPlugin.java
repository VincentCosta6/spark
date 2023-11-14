package com.github.spark.lib;

import com.github.spark.lib.events.PlayerCommand;
import com.github.spark.lib.framework.Framework;
import com.github.spark.lib.services.custom.MetadataService;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.logging.Level;

public abstract class SparkPlugin extends JavaPlugin {
    public Framework framework;
    private Set<Supplier<?>> customServices = new HashSet<>();

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
        framework.registerSingletonServiceFactory(MetadataService.class, MetadataService::new);
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
