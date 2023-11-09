package live.artgen.paperframework.impl;

import live.artgen.paperframework.impl.commands.TestCommand;
import live.artgen.paperframework.impl.datastores.PlayerStateDataStore;
import live.artgen.paperframework.impl.events.PlayerMove;
import live.artgen.paperframework.lib.FrameworkPlugin;

public final class PluginEntry extends FrameworkPlugin {
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
