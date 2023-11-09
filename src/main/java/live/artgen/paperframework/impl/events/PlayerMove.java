package live.artgen.paperframework.impl.events;

import live.artgen.paperframework.impl.datastores.PlayerState;
import live.artgen.paperframework.impl.datastores.PlayerStateDataStore;
import live.artgen.paperframework.lib.Framework;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

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
