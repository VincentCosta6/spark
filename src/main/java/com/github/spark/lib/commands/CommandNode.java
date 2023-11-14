package com.github.spark.lib.commands;

import com.github.spark.lib.commands.dto.CommandContext;
import com.github.spark.lib.commands.dto.CommandNodeExecutionContext;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandNode {
    private final String name;
    private final String description;
    private final Method handler;
    private final Command command;

    private CommandNode parentCommand;
    private final Map<String, CommandNode> subCommands = new HashMap<>();

    public CommandNode(CommandNode parentCommand, String name, String description, Command command, Method handler) {
        this.parentCommand = parentCommand;
        this.name = name;
        this.description = description;
        this.handler = handler;
        this.command = command;
    }

    public CommandNode(CommandNode parentCommand, String name, String description, Command command) {
        this.parentCommand = parentCommand;
        this.name = name;
        this.description = description;
        this.command = command;
        this.handler = null;
    }

    public void addSubCommand(CommandNode subCommand) {
        subCommands.put(subCommand.getName(), subCommand);
    }

    public boolean execute(CommandContext context) {
        try {
            if (this.handler == null) {
                if (context.executionContext().restPaths().length == 0) {
                    return command.onCommand(context);
                }

                String nextPath = context.executionContext().restPaths()[0];
                CommandNode subCommand = subCommands.get(nextPath.toLowerCase());
                if (subCommand != null) {
                    String nextParam = null;
                    if (context.executionContext().restPaths().length > 1) {
                        nextParam = context.executionContext().restPaths()[1];
                    }
                    CommandContext newCommandContext = new CommandContext(
                        context,
                            context.player(),
                            context.event(),
                            context.playerCommandEvent(),
                            new CommandNodeExecutionContext(
                                nextPath,
                                Arrays.stream(context.executionContext().restPaths()).skip(1).toArray(String[]::new)
                            ),
                            nextParam != null,
                            nextParam
                    );
                    return subCommand.execute(newCommandContext);
                }

                return command.onCommand(context);
            } else {
                return (Boolean) handler.invoke(this.command, context);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Getter methods
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Map<String, CommandNode> getSubCommands() { return subCommands; }
}
