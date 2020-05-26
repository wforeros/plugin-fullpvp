package me.theoldestwilly.fullpvp.event.scheduler;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ScheduledEventStartEvent extends Event implements Cancellable {
    public static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private String scheduledEventName;
    private ScheduledEvent scheduledEvent;

    public ScheduledEventStartEvent(ScheduledEvent scheduledEvent) {
        this.scheduledEventName = scheduledEvent.getEventName();
        this.scheduledEvent = scheduledEvent;
    }

    public String getScheduledlEventName() {
        return scheduledEventName;
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