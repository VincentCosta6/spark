package com.github.spark.examples.events;

import com.github.spark.examples.datastores.PlayerState;
import com.github.spark.examples.datastores.PlayerStateDataStore;
import com.github.spark.lib.Framework;
import org.bukkit.Location;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.google.inject.Inject;
import org.bukkit.util.Vector;

public class PlayerMove implements Listener {
    @Inject Framework framework;
    @Inject PlayerStateDataStore playerStateDataStore;

    @EventHandler
    public void onPlayerMove(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerState playerState = playerStateDataStore.getOrDefaultCreate(player);

        framework.log(playerState.toString());
        playerState.magicka -= 1;
        framework.saveDataStores();



        int distanceToSpawn = 1;
        int fireballVelocity = 5;

        Location eyeLocation = player.getEyeLocation();
        Vector direction = eyeLocation.getDirection();

        Location fireballSpawnPoint = eyeLocation.add(direction.clone().multiply(distanceToSpawn));

        Fireball fireball = player.getWorld().spawn(fireballSpawnPoint, Fireball.class);
        fireball.setDirection(direction);
        fireball.setVelocity(direction.multiply(fireballVelocity));

//        player.getWorld().
    }
}
