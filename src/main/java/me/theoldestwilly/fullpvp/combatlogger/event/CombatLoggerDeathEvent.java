package me.theoldestwilly.fullpvp.combatlogger.event;

import lombok.Getter;
import me.theoldestwilly.fullpvp.combatlogger.CombatLogger;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CombatLoggerDeathEvent extends Event implements Cancellable {
    private static HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private @Getter Player killer;
    private @Getter
    CombatLogger combatLogger;

    public CombatLoggerDeathEvent(Player killer, CombatLogger combatLogger) {
        this.killer = killer;
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
