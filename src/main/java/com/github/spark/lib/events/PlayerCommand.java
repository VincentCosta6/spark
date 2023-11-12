package com.github.spark.lib.events;

import com.github.spark.lib.commands.dto.CommandContext;
import com.github.spark.lib.commands.CommandNode;
import com.github.spark.lib.commands.dto.CommandNodeExecutionContext;
import com.github.spark.lib.commands.dto.PlayerCommandEvent;
import com.google.inject.Inject;
import com.github.spark.lib.Framework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;

public class PlayerCommand implements Listener {
    @Inject Framework framework;

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        int index = event.getMessage().indexOf(' ');
        String rootCommand = event.getMessage().substring(1, index != -1 ? index : event.getMessage().length());

        CommandNode node = framework.commandRegistry.getCommand(rootCommand);
        if (node != null) {
            String[] path = event.getMessage().split(" ");

            CommandContext newContext = new CommandContext(
                null,
                new PlayerCommandEvent(event.getPlayer(), path, rootCommand),
                new CommandNodeExecutionContext(rootCommand, Arrays.stream(path).skip(1).toArray(String[]::new))
            );
            node.execute(newContext);
            return;
        }
    }
}
