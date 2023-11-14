package com.github.spark.lib.commands.dto;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public record CommandContext(CommandContext parentContext, Player player, PlayerCommandPreprocessEvent event, PlayerCommandEvent playerCommandEvent, CommandNodeExecutionContext executionContext, boolean hasMoreParams) {
}
