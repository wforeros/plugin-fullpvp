package me.theoldestwilly.fullpvp.clan.events;

import lombok.Getter;
import me.theoldestwilly.fullpvp.clan.Clan;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import java.util.List;

public class ClanDisbandEvent extends ClanEvent implements Cancellable {

    private @Getter List<Player> clanMembers;

    public ClanDisbandEvent(Player leader, Clan clan) {
        super(leader, clan);
        clanMembers = clan.getClanMembersAsPlayers();
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean b) {

    }


}
