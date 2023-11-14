package com.github.spark.examples.events;

import com.github.spark.examples.constants.Constants;
import com.github.spark.examples.datastores.PlayerState;
import com.github.spark.examples.datastores.PlayerStateDataStore;
import com.github.spark.examples.services.CustomService;
import com.github.spark.lib.framework.Framework;
import com.github.spark.lib.events.annotations.RegisterEvents;
import com.github.spark.lib.services.custom.MetadataService;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.google.inject.Inject;

@RegisterEvents
public class PlayerMove implements Listener {
    @Inject Framework framework;
    @Inject PlayerStateDataStore playerStateDataStore;
    @Inject CustomService customService;
    @Inject MetadataService metaService;

    @EventHandler
    public void onPlayerMove(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerState playerState = playerStateDataStore.getOrDefaultCreate(player);

        framework.log(playerState.toString());
        playerState.magicka -= 1;
        customService.printTestAndPlayerStateCount();

        framework.log(event.getItem().toString());

        if (metaService.getMetaBoolean(event.getItem(), Constants.MAGIC_WAND_KEY) == Boolean.TRUE) {
            player.sendMessage(Component.text("You used a magic wand!"));
        }
    }
}
