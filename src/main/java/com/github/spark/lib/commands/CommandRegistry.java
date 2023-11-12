package com.github.spark.lib.commands;

import com.github.spark.lib.Framework;
import com.github.spark.lib.SparkContext;
import com.github.spark.lib.commands.dto.CommandContext;
import org.checkerframework.checker.units.qual.A;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
