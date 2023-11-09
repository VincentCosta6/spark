package live.artgen.paperframework.impl.datastores;

import live.artgen.paperframework.lib.datastores.DataStore;
import org.bukkit.entity.Player;

import java.util.function.Supplier;

public class PlayerStateDataStore extends DataStore<PlayerState> {
    public PlayerState getByPlayer(Player player) {
        return findById(player.getUniqueId().toString());
    }

    public PlayerState getOrDefaultCreate(Player player) {
        return super.getOrDefaultCreate(player.getUniqueId().toString(), () -> PlayerState.createDefault(player));
    }

    @Override
    public PlayerState getOrDefaultCreate(Player player, Supplier<PlayerState> defaultCreator) {
        return super.getOrDefaultCreate(player.getUniqueId().toString(), defaultCreator);
    }
}
