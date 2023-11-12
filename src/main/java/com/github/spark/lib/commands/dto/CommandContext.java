package com.github.spark.lib.commands.dto;

public record CommandContext(CommandContext parentContext, PlayerCommandEvent playerCommandEvent, CommandNodeExecutionContext executionContext) {
}
