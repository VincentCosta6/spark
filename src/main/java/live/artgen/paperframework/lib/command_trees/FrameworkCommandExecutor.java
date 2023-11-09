package live.artgen.paperframework.lib.command_trees;

public interface FrameworkCommandExecutor {
    boolean onCommand(PlayerCommandEvent event, CommandTreeExecutionContext context);
}
