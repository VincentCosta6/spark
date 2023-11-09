package live.artgen.paperframework.impl.datastores;

import live.artgen.paperframework.lib.datastores.DataStoreItem;
import org.bukkit.entity.Player;

public class PlayerState extends DataStoreItem {
    public String playerId;
    public int magicka = 100;

    @Override
    public String getItemId() {
        return playerId;
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
