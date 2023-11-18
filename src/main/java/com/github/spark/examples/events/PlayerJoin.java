package com.github.spark.examples.events;

import com.github.spark.examples.services.ScoreboardService;
import com.github.spark.lib.events.annotations.RegisterEvents;
import com.google.inject.Inject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RegisterEvents
public class PlayerJoin implements Listener {
    @Inject ScoreboardService scoreboardService;
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        scoreboardService.updateScoreboard(event.getPlayer());
    }
}
