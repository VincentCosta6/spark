package com.github.spark.examples.services;

import com.github.spark.examples.datastores.PlayerState;
import com.github.spark.examples.datastores.PlayerStateDataStore;
import com.github.spark.lib.framework.Framework;
import com.github.spark.lib.observables.annotations.ObserveMutation;
import com.github.spark.lib.services.annotations.Service;
import com.google.inject.Inject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.UUID;

@Service
public class ScoreboardService {
    @Inject Framework framework;
    @Inject PlayerStateDataStore playerStateDataStore;

    public void updateScoreboard(Player player) {
        PlayerState playerState = playerStateDataStore.getOrDefaultCreate(player);

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective o = board.registerNewObjective("Class Level", "");
        o.setDisplaySlot(DisplaySlot.SIDEBAR);
        o.setDisplayName(ChatColor.DARK_AQUA + "Navarta");

        Score mana = o.getScore(ChatColor.WHITE + "Mana: " + ChatColor.GOLD + playerState.magicka);
        mana.setScore(3);

        player.setScoreboard(board);
    }

    @ObserveMutation
    private void onMutation(PlayerState state) {
        Player player = Bukkit.getPlayer(UUID.fromString(state.playerId));
        updateScoreboard(player);
        framework.log("1: " + state.toString());
    }
}
