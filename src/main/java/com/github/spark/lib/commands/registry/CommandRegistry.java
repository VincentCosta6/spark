package com.github.spark.lib.commands.registry;

import com.github.spark.lib.commands.CommandNode;
import com.github.spark.lib.common.Registry;
import com.github.spark.lib.framework.Framework;

import java.util.ArrayList;

public class CommandRegistry extends Registry<String, CommandNode> {

    public CommandRegistry(Framework framework) {
        super(framework);
    }

    @Override
    public void findAndRegisterItems() {
        ArrayList<CommandNode> commands = CommandReflection.findCommandNodes(framework);
        commands.forEach(commandNode -> add(commandNode.getName(), commandNode));
    }
}
