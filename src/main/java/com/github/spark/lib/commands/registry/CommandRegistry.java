package com.github.spark.lib.commands.registry;

import com.github.spark.lib.commands.CommandNode;
import com.github.spark.lib.commands.registry.CommandReflection;
import com.github.spark.lib.common.Registry;
import com.github.spark.lib.framework.Framework;
import com.github.spark.lib.framework.FrameworkInjectable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommandRegistry extends Registry<String, CommandNode> {

    public CommandRegistry(Framework framework) {
        super(framework);
    }

    @Override
    public void findAndRegisterItems() {
        ArrayList<CommandNode> commands = CommandReflection.findCommandNodes(framework);
        commands.forEach(commandNode -> addItem(commandNode.getName(), commandNode));
    }
}
