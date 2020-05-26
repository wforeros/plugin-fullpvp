package me.theoldestwilly.fullpvp.clan.events;

import lombok.Getter;
import me.theoldestwilly.fullpvp.clan.Clan;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlayerLeaveClanEvent extends ClanEvent implements Cancellable{
    private boolean cancelled;
    public PlayerLeaveClanEvent(Player who, Clan clan) {
        super(who, clan);
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
