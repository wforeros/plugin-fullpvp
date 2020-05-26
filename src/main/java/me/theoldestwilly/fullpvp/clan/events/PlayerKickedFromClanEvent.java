package me.theoldestwilly.fullpvp.clan.events;

import lombok.Getter;
import me.theoldestwilly.fullpvp.clan.Clan;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlayerKickedFromClanEvent extends ClanEvent implements Cancellable {
    private boolean cancelled;
    private @Getter OfflinePlayer kicked;
    public PlayerKickedFromClanEvent(Player kicker, OfflinePlayer kicked, Clan clan) {
        super(kicker, clan);
        this.kicked = kicked;
    }

    public boolean isKickedPlayerOnline() {
        return kicked.isOnline();
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}
