package me.theoldestwilly.fullpvp.kits.event;

import me.theoldestwilly.fullpvp.kits.Kit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerKitApplyEvent extends PlayerKitEvent implements Cancellable {
    private static HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final boolean forced;

    public PlayerKitApplyEvent(Player player, Kit kit, boolean forced) {
        super(player, kit);
        this.forced = forced;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isForced() {
        return this.forced;
    }
}