package com.github.spark.lib.events;

import com.github.spark.lib.commands.dto.CommandContext;
import com.github.spark.lib.commands.CommandNode;
import com.github.spark.lib.commands.dto.CommandNodeExecutionContext;
import com.github.spark.lib.commands.dto.PlayerCommandEvent;
import com.google.inject.Inject;
import com.github.spark.lib.framework.Framework;
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

        CommandNode node = framework.commandRegistry.getItem(rootCommand.toLowerCase());
        if (node != null) {
            String[] path = event.getMessage().split(" ");
            String nextParam = null;
            if (path.length > 1) {
                nextParam = path[1];
            }

            CommandContext newContext = new CommandContext(
                null,
                    player,
                    event,
                new PlayerCommandEvent(event.getPlayer(), path, rootCommand),
                new CommandNodeExecutionContext(rootCommand, Arrays.stream(path).skip(1).toArray(String[]::new)),
                    nextParam != null,
                    nextParam
            );
            node.execute(newContext);
            return;
        }
    }
}
