package me.theoldestwilly.fullpvp.event.scheduler;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ScheduledEventFinishEvent extends Event {
    public static final HandlerList handlers = new HandlerList();
    
    private String scheduledEventName;
    public ScheduledEventFinishEvent(String scheduledEventName) {
        this.scheduledEventName = scheduledEventName;
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

}
