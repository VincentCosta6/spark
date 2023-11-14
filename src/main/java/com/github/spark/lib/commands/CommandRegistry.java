package com.github.spark.lib.commands;

import com.github.spark.lib.framework.Framework;
import com.github.spark.lib.framework.FrameworkInjectable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommandRegistry implements FrameworkInjectable {
    private final Framework framework;
    private final Map<String, CommandNode> commands = new HashMap<>();

    public CommandRegistry(Framework framework) {
        this.framework = framework;
    }

    public void addCommand(String name, CommandNode node) {
        commands.put(name, node);
    }

    public CommandNode getCommand(String name) {
        return commands.get(name);
    }

    public void findAndRegisterCommands() {
        ArrayList<CommandNode> commands = CommandReflection.findCommandNodes(framework);
        commands.forEach(commandNode -> addCommand(commandNode.getName(), commandNode));
    }

    public boolean injectMembers() {
        for(Object service : commands.values()) {
            framework.injectMembers(service);
        }
        return true;
    }
}
