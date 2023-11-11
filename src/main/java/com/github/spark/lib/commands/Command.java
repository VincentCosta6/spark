package com.github.spark.lib.commands;

import com.github.spark.lib.commands.dto.CommandContext;

public interface Command {
    boolean onCommand(CommandContext context);
}
