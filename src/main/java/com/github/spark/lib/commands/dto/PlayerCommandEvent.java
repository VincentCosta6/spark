package com.github.spark.lib.commands.dto;

import org.bukkit.entity.Player;

public record PlayerCommandEvent(Player player, String[] allArgs, String root) {}