package com.github.spark.examples.events;

import com.github.spark.examples.datastores.PlayerState;
import com.github.spark.examples.datastores.PlayerStateDataStore;
import com.github.spark.examples.services.CustomService;
import com.github.spark.lib.framework.Framework;
import com.github.spark.lib.events.annotations.RegisterEvents;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.google.inject.Inject;

@RegisterEvents
public class PlayerMove implements Listener {
    @Inject
    Framework framework;
    @Inject PlayerStateDataStore playerStateDataStore;
    @Inject CustomService customService;

    @EventHandler
    public void onPlayerMove(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerState playerState = playerStateDataStore.getOrDefaultCreate(player);

        framework.log(playerState.toString());
        playerState.magicka -= 1;
        customService.printTestAndPlayerStateCount();
    }
}
