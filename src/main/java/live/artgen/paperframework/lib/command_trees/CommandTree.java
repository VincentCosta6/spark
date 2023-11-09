package live.artgen.paperframework.lib.command_trees;

import com.google.inject.Inject;
import live.artgen.paperframework.lib.Framework;

import java.util.Arrays;
import java.util.HashMap;

public abstract class CommandTree implements FrameworkCommandExecutor {
    @Inject public Framework framework;
    public String name = "";
    public HashMap<String, CommandTree> nodes = new HashMap<>();

    public CommandTree() {}
    public CommandTree(String name) { this.name = name; }

    public void addSubCommand(CommandTree subCommand) {
        subCommand.framework = this.framework;
        this.nodes.put(subCommand.name, subCommand);
    }

    public void addSubCommand(String name, CommandTree subCommand) {
        subCommand.framework = this.framework;
        subCommand.name = name;
        this.nodes.put(name, subCommand);
    }

    public void onCommandCreate() {

    }

    public void runRoot(PlayerCommandEvent event, CommandTreeExecutionContext context) {
        if (context.restPaths().length == 0) {
            this.onCommand(event, context);
            return;
        }

        String nextCommand = context.restPaths()[0];
        CommandTree subTree = this.nodes.get(nextCommand);
        if (subTree != null) {
            subTree.runRoot(
                event,
                new CommandTreeExecutionContext(
                    nextCommand,
                    Arrays.stream(context.restPaths()).skip(1).toArray(String[]::new)
                )
            );
            return;
        }

        this.onCommand(event, context);
    }

    public abstract boolean onCommand(PlayerCommandEvent event, CommandTreeExecutionContext context);

    public void setCommandName(String name) {
        this.name = name;
    }
}
