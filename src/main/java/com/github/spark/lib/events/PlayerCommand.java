package com.github.spark.lib.events;

import com.github.spark.lib.command_trees.CommandTree;
import com.github.spark.lib.command_trees.CommandTreeExecutionContext;
import com.github.spark.lib.command_trees.PlayerCommandEvent;
import com.github.spark.lib.commands.dto.CommandContext;
import com.github.spark.lib.commands.dto.CommandNode;
import com.github.spark.lib.commands.dto.CommandNodeExecutionContext;
import com.google.inject.Inject;
import com.github.spark.lib.Framework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;
import java.util.logging.Level;

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

        CommandTree treeExists = framework.commands.get(rootCommand);
        if (treeExists != null) {
            try {
                String[] path = event.getMessage().split(" ");

                PlayerCommandEvent commandEvent = new PlayerCommandEvent(
                    player,
                    path,
                    rootCommand
                );

                CommandTreeExecutionContext context = new CommandTreeExecutionContext(
                    rootCommand,
                    Arrays.stream(path).skip(1).toArray(String[]::new)
                );

                treeExists.runRoot(commandEvent, context);
            } catch (Exception e) {
                framework.log(Level.WARNING, "Error occurred while running command: " + rootCommand);
                framework.log(Level.WARNING, e.getMessage());
            }
        }
    }
}
