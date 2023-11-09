package live.artgen.paperframework.lib;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import live.artgen.paperframework.lib.command_trees.CommandTree;
import live.artgen.paperframework.lib.datastores.DataStore;
import live.artgen.paperframework.lib.datastores.DataStoreItem;
import live.artgen.paperframework.lib.datastores.DataStoreRegistry;
import live.artgen.paperframework.lib.modules.FrameworkModule;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

public class Framework {
    public HashMap<String, CommandTree> commands = new HashMap<>();
    public DataStoreRegistry dataStoreRegistry;

    private final FrameworkPlugin plugin;
    private Injector injector;

    public Framework(FrameworkPlugin plugin) {
        this.plugin = plugin;
        dataStoreRegistry = new DataStoreRegistry(this);
    }

    public void addCommand(CommandTree command) {
        injector.injectMembers(command);

        commands.put(command.name, command);
        command.onCommandCreate();
    }

    public void addListener(Listener listener) {
        injector.injectMembers(listener);

        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
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

    public void log(String message) {
        plugin.getLogger().log(Level.INFO, message);
    }

    public void log(Level level, String message) {
        plugin.getLogger().log(level, message);
    }

    public void log(Level level, String message, boolean internal) {
        log(level, (internal ? "[FRAMEWORK] " : "") + message);
    }
}
