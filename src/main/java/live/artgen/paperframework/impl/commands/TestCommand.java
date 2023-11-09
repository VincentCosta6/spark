package live.artgen.paperframework.impl.commands;

import com.google.inject.Inject;
import live.artgen.paperframework.lib.Framework;
import live.artgen.paperframework.lib.command_trees.CommandTree;
import live.artgen.paperframework.lib.command_trees.CommandTreeExecutionContext;
import live.artgen.paperframework.lib.command_trees.PlayerCommandEvent;

public class TestCommand extends CommandTree {
    @Override
    public void onCommandCreate() {
        this.setCommandName("test");
        this.addSubCommand("add", new Arg1());
    }

    @Override
    public boolean onCommand(PlayerCommandEvent event, CommandTreeExecutionContext context) {
        event.player().sendMessage("Hi");
        framework.log(framework.commands.size() + "");
        return true;
    }
}

class Arg1 extends CommandTree {
    @Override
    public boolean onCommand(PlayerCommandEvent event, CommandTreeExecutionContext context) {
        event.player().sendMessage("Hi from arg1");
        framework.log(framework.commands.size() + "");
        framework.saveDataStores();
        return true;
    }
}
