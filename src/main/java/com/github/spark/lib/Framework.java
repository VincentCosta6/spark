package com.github.spark.lib;

import com.github.spark.lib.command_trees.CommandTree;
import com.github.spark.lib.commands.CommandRegistry;
import com.github.spark.lib.datastores.DataStore;
import com.github.spark.lib.datastores.DataStoreRegistry;
import com.github.spark.lib.modules.FrameworkModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;

public class Framework {
    public HashMap<String, CommandTree> commands = new HashMap<>();

    public CommandRegistry commandRegistry;
    public DataStoreRegistry dataStoreRegistry;

    private final SparkPlugin plugin;
    private Injector injector;

    public Framework(SparkPlugin plugin) {
        this.plugin = plugin;
        dataStoreRegistry = new DataStoreRegistry(this);
        commandRegistry = new CommandRegistry(this);
    }

    @Deprecated
    public void addCommand(CommandTree command) {
        injector.injectMembers(command);

        commands.put(command.name, command);
        command.onCommandCreate();
    }

    public void addListener(Listener listener) {
        injector.injectMembers(listener);

        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    public void loadCommands() {
        commandRegistry.findAndRegisterCommands();
    }

    public void handleStoresLoaded() {
        injector = Guice.createInjector(new FrameworkModule(this, dataStoreRegistry));
    }

    public <T extends DataStore<?>> void addDataStore(T store) {
        dataStoreRegistry.addDataStore(store);
        store.onLoad(plugin.getDataFolder());
    }

    public void saveDataStores() {
        File dataFolder = plugin.getDataFolder();
        for (var it = dataStoreRegistry.getEntries(); it.hasNext(); ) {
            var store = it.next();
            store.onSave(dataFolder);
        }
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
