package me.theoldestwilly.fullpvp.combatlogger.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class TagApplicationEvent extends PlayerEvent implements Cancellable {
    private static HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    private Player damager;
    public TagApplicationEvent(Player who, Entity damager) {
        super(who);
        if (damager instanceof Player) this.damager = (Player) damager;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    public Player getDamager() {
        return damager;
    }
}
