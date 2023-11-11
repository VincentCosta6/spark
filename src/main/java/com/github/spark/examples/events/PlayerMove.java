package com.github.spark.examples.events;

import com.github.spark.examples.datastores.PlayerState;
import com.github.spark.examples.datastores.PlayerStateDataStore;
import com.github.spark.lib.Framework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.google.inject.Inject;

public class PlayerMove implements Listener {
    @Inject Framework framework;
    @Inject PlayerStateDataStore playerStateDataStore;

    @EventHandler
    public void onPlayerMove(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerState playerState = playerStateDataStore.getOrDefaultCreate(player);

        framework.log(playerState.toString());
        playerState.magicka -= 1;
    }
}
