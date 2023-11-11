package com.github.spark.lib.commands.dto;

import com.github.spark.lib.command_trees.PlayerCommandEvent;

public record CommandContext(CommandContext parentContext, PlayerCommandEvent playerCommandEvent, CommandNodeExecutionContext executionContext) {
}
