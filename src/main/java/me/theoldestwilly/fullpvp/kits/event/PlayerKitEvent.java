package me.theoldestwilly.fullpvp.kits.event;

import me.theoldestwilly.fullpvp.kits.Kit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;

public abstract class PlayerKitEvent extends PlayerEvent {
    protected final Kit kit;

    public PlayerKitEvent(Player player, Kit kit) {
        super(player);
        this.kit = kit;
    }

    public Kit getKit() {
        return this.kit;
    }
}
