package com.github.spark.examples.datastores;

import com.github.spark.lib.datastores.DataStoreItem;
import com.github.spark.lib.datastores.ItemKey;
import org.bukkit.entity.Player;

public class PlayerState extends DataStoreItem {
    @ItemKey public String playerId;
    public int magicka = 100;
    public float cash = 10000.0f;

    public float getCash() {
        return this.cash;
    }

    public static PlayerState createDefault(Player player) {
        PlayerState newState = new PlayerState();
        newState.magicka = 100;
        newState.playerId = player.getUniqueId().toString();
        return newState;
    }

    @Override
    public String toString() {
        return "PlayerState{" +
                "playerId='" + playerId + '\'' +
                ", magicka=" + magicka +
                '}';
    }
}
