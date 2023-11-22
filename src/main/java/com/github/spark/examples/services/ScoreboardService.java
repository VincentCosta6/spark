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

        Score mana = o.getScore(ChatColor.WHITE + "Mana: " + ChatColor.LIGHT_PURPLE + playerState.magicka);
        mana.setScore(3);

        player.setScoreboard(board);
    }

    @ObserveMutation
    private void onMutation(PlayerState state) {
        Player player = Bukkit.getPlayer(UUID.fromString(state.playerId));
        updateScoreboard(player);
        System.out.println("old method");
        System.out.println(state.toString());
    }

    @ObserveMutation
    private void onMutation(PlayerState state, PlayerState oldState) {
        Player player = Bukkit.getPlayer(UUID.fromString(state.playerId));
        updateScoreboard(player);
        System.out.println("new method");
        System.out.println(state.toString());
        System.out.println(oldState.toString());
    }
}
