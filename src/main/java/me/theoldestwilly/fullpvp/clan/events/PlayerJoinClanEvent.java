package me.theoldestwilly.fullpvp.clan.events;

import lombok.Getter;
import me.theoldestwilly.fullpvp.clan.Clan;
import me.theoldestwilly.fullpvp.users.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerJoinClanEvent extends ClanEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private @Getter Profile playerProfile;

    public PlayerJoinClanEvent(Player who, Clan clan, Profile playerProfile) {
        super(who, clan);
        this.playerProfile = playerProfile;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
