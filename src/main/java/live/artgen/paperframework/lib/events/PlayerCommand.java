package live.artgen.paperframework.lib.events;

import com.google.inject.Inject;
import live.artgen.paperframework.lib.Framework;
import live.artgen.paperframework.lib.command_trees.CommandTree;
import live.artgen.paperframework.lib.command_trees.CommandTreeExecutionContext;
import live.artgen.paperframework.lib.command_trees.PlayerCommandEvent;
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
