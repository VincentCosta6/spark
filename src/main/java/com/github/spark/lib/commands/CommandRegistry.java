package com.github.spark.lib.commands;

import com.github.spark.lib.Framework;
import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {
    private final Map<String, CommandNode> commands = new HashMap<>();
    private Framework framework;

    public CommandRegistry(Framework framework) {
        this.framework = framework;
    }

    public void addCommand(String name, CommandNode node) {
        commands.put(name, node);
    }

    public CommandNode getCommand(String name) {
        return commands.get(name);
    }
}
