package com.github.spark.lib.commands;

public interface Command {
    boolean onCommand(CommandEventContext context);
}
