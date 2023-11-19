package com.github.spark.lib.framework;

import com.github.spark.lib.SparkPlugin;
import com.github.spark.lib.commands.registry.CommandRegistry;
import com.github.spark.lib.datastores.registry.DataStoreRegistry;
import com.github.spark.lib.events.registrry.EventRegistry;
import com.github.spark.lib.modules.FrameworkModule;
import com.github.spark.lib.observables.registry.ObservableRegistry;
import com.github.spark.lib.services.registry.ServiceRegistry;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.function.Supplier;
import java.util.logging.Level;

public class Framework {
    public CommandRegistry commandRegistry;
    public DataStoreRegistry dataStoreRegistry;
    public ServiceRegistry serviceRegistry;
    public EventRegistry eventRegistry;
    public ObservableRegistry observableRegistry;

    public final SparkPlugin plugin;
    private Injector injector;

    public Framework(SparkPlugin plugin) {
        this.plugin = plugin;
        // these are injectable into other classes
        dataStoreRegistry = new DataStoreRegistry(this);
        serviceRegistry = new ServiceRegistry(this);

        // these are not
        commandRegistry = new CommandRegistry(this);
        eventRegistry = new EventRegistry(this);
        observableRegistry = new ObservableRegistry(this);
    }

    public void addListener(Listener listener) {
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    public void createInjector() {
        injector = Guice.createInjector(
            new FrameworkModule(this,
                    dataStoreRegistry,
                    serviceRegistry
            ));
    }

    public void saveDataStores() {
        saveDataStores(false);
    }

    public void saveDataStores(boolean autoSave) {
        File dataFolder = plugin.getDataFolder();
        for (var it = dataStoreRegistry.getEntries(); it.hasNext(); ) {
            var store = it.next();
            if (!autoSave || store.isDirty()) {
                store.onSave(dataFolder);
            }
        }
    }

    public <T> void registerSingletonServiceFactory(Class<T> clazz, Supplier<T> singletonFactory) {
        serviceRegistry.registerSingletonServiceFactory(clazz, singletonFactory);
    }

    public void injectMembers(Object object) {
        injector.injectMembers(object);
    }

    public void log(String message) {
        plugin.getLogger().log(Level.INFO, message);
    }

    public void log(Level level, String message) {
        plugin.getLogger().log(level, message);
    }

    public void log(Level level, String message, boolean internal) {
        log(level, (internal ? "[SPARK] " : "") + message);
    }
}
