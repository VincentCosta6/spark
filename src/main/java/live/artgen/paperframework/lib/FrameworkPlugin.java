package live.artgen.paperframework.lib;

import live.artgen.paperframework.lib.events.PlayerCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public abstract class FrameworkPlugin extends JavaPlugin {
    public Framework framework;

    /**
     * This is called after all commands and listeners have been registered
     */
    public abstract void onFrameworkEnable();

    @Override
    public final void onEnable() {
        getLogger().log(Level.INFO, "[FRAMEWORK] Initializing Framework...");

        if (!getDataFolder().exists()) {
            getLogger().log(Level.INFO, "[FRAMEWORK] Data folder not found creating...");
            getDataFolder().mkdir();
        }

        this.framework = new Framework(this);

        /////////// LOAD PLUGIN STORES ///////////////////
        framework.log(Level.INFO, "Registering plugin datastores...", true);
        registerDataStores();

        this.framework.handleStoresLoaded();

        /////////// LOAD INTERNAL COMMANDS ///////////////////
        framework.log(Level.INFO, "Registering framework commands...", true);
        onLoadInternalCommands();
        int internalCommandSize = framework.commands.size();
        framework.log(Level.INFO, "Loaded " + internalCommandSize + " internal command(s)", true);

        /////////// LOAD PLUGIN COMMANDS ///////////////////
        framework.log(Level.INFO, "Registering plugin commands", true);
        this.registerCommands();
        int newPluginCommandSize = framework.commands.size() - internalCommandSize;
        framework.log(Level.INFO, "Loaded " + newPluginCommandSize + " plugin command(s)", true);

        this.registerListeners();

        this.onFrameworkEnable();
    }

    @Override
    public void onDisable() {
        framework.saveDataStores();
    }

    private void onLoadInternalCommands() {
        framework.addListener(new PlayerCommand());
    }

    public void registerCommands() {}
    public void registerListeners() {}
    public void registerDataStores() {}
}
