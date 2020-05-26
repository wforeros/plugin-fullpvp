package me.theoldestwilly.fullpvp.combatlogger.event;

import lombok.Getter;
import me.theoldestwilly.fullpvp.combatlogger.CombatLogger;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CombatLoggerDamageByPlayerEvent extends Event implements Cancellable {
    private static HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private @Getter Player damager;
    private @Getter
    CombatLogger combatLogger;

    public CombatLoggerDamageByPlayerEvent(Player damager, CombatLogger combatLogger) {
        this.damager = damager;
        this.combatLogger = combatLogger;
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
}
