package com.github.spark.lib.commands.dto;

import com.github.spark.lib.commands.Command;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CommandNode {
    private final String name;
    private final String description;
    private final Method handler;
    private final Command command;

    private CommandNode parentCommand;
    private final Map<String, CommandNode> subCommands = new HashMap<>();

    public CommandNode(CommandNode parentCommand, String name, String description, Method handler) {
        this.parentCommand = parentCommand;
        this.name = name;
        this.description = description;
        this.handler = handler;
        this.command = null;
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
            if (this.command != null) {
                command.onCommand(null);
            } else {
                handler.invoke(handler, context);
            }
            return true;
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
