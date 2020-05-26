package me.theoldestwilly.fullpvp.event.hosted.structure;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class HostedTeamsEvent extends HostedEvent {

    public HostedTeamsEvent(Player hoster, HostedEventType type) {
        super(hoster, type, true);
    }

    @Override
    public boolean hasEnded() {
        return false;
    }

    @Override
    public void onFinish() {
        String winners = "";
        int counter = 0;
        int max = getAlivePlayers().size();
        for (Player player : getAlivePlayers()) {
            removePlayer(player, null, false, false);
            setRewards(player);
            winners += (player != null ?  player.getDisplayName() : "");
            ++counter;
            if (max > counter) {
                winners += ChatColor.GRAY + ", ";
            } else {
                winners += ChatColor.GRAY + ".";
            }
        }
        Bukkit.broadcastMessage(getPrefix() + "Congratulations to the event winners!");
    }


}
