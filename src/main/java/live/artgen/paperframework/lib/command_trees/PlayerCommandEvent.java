package live.artgen.paperframework.lib.command_trees;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public record PlayerCommandEvent(Player player, String[] allArgs, String root) {}
