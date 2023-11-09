package live.artgen.paperframework.lib.commands;

public interface Command {
    boolean onCommand(CommandEventContext context);
}
