package me.theoldestwilly.fullpvp.clan.events;

import com.google.common.base.Preconditions;
import lombok.Getter;
import me.theoldestwilly.fullpvp.clan.Clan;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public abstract class ClanEvent extends PlayerEvent {

    public static final HandlerList handlers = new HandlerList();

    @Getter private Clan clan;
    public ClanEvent(Player who, Clan clan) {
        super(who);
        Preconditions.checkNotNull(clan, "Clan can not be null.");
        this.clan = clan;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
