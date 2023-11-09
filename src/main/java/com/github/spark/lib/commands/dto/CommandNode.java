package com.github.spark.lib.commands.dto;

import java.util.HashMap;
import java.util.Map;

public class CommandNode {
    private String name;
    private String description;
    private Map<String, CommandNode> subCommands = new HashMap<>();

    public CommandNode(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void addSubCommand(CommandNode subCommand) {
        subCommands.put(subCommand.getName(), subCommand);
    }

    // Getter methods
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Map<String, CommandNode> getSubCommands() { return subCommands; }
}
