package com.github.spark.examples.events;

import com.github.spark.examples.constants.Constants;
import com.github.spark.examples.datastores.PlayerState;
import com.github.spark.examples.datastores.PlayerStateDataStore;
import com.github.spark.examples.services.CustomService;
import com.github.spark.lib.framework.Framework;
import com.github.spark.lib.events.annotations.RegisterEvents;
import com.github.spark.lib.services.custom.MetadataService;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.google.inject.Inject;
import org.bukkit.util.Vector;

@RegisterEvents
public class PlayerMove implements Listener {
    @Inject Framework framework;
    @Inject PlayerStateDataStore playerStateDataStore;
    @Inject CustomService customService;
    @Inject MetadataService metaService;

    @EventHandler
    public void onPlayerMove(PlayerInteractEvent event) {
        if (!event.hasItem()) {
            return;
        }

        Player player = event.getPlayer();

        if (metaService.getMetaBoolean(event.getItem(), Constants.MAGIC_WAND_KEY) == Boolean.TRUE) {
            PlayerState playerState = playerStateDataStore.getOrDefaultCreate(player);

            playerState.mutate(() -> {
                playerState.magicka -= 1;
            });
            int distanceToSpawn = 2;
            double fireballVelocity = 0.8;

            Location eyeLocation = player.getEyeLocation();
            Vector direction = eyeLocation.getDirection();

            Location fireballSpawnPoint = eyeLocation.add(direction.clone().multiply(distanceToSpawn));
            Fireball fireball = player.getWorld().spawn(fireballSpawnPoint, Fireball.class);
            fireball.setDirection(direction);
            fireball.setYield(100);
            fireball.setVelocity(direction.normalize().multiply(fireballVelocity));
        }
    }
}
