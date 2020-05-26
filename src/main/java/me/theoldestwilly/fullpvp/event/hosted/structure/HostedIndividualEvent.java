package me.theoldestwilly.fullpvp.event.hosted.structure;

import me.theoldestwilly.fullpvp.users.Profile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import java.util.Collections;
import java.util.List;

public abstract class HostedIndividualEvent extends HostedEvent {

    public HostedIndividualEvent(Player hoster, HostedEventType type, boolean gracePeriodEnabled) {
        super(hoster, type, gracePeriodEnabled);
    }

    /**
     * This boolean is used to check if alive players amount is equal to 1
     * @return
     */
    @Override
    public boolean hasEnded() {
        return getAlivePlayers().size() <= 1;
    }

    @Override
    public void onFinish() {
        if (hasEnded()) {
            Player player = getAlivePlayers().get(0);
            setWinnerSelected(true);
            removePlayer(player, null, false, false);
            removeSpectator(player);
            setRewards(player);
            addEventWinToWinnersProfiles(Collections.singletonList(player));
            HandlerList.unregisterAll(this);
            getPlugin().getSpawnHandler().directSpawnTeleport(player, "main");
            Bukkit.broadcastMessage(getPrefix() + ChatColor.YELLOW + "Congratulations to " + player.getDisplayName() + ChatColor.YELLOW + " for winning the " + getDisplayName() + ChatColor.YELLOW + " event!");
        }
    }

    @Override
    public void addEventWinToWinnersProfiles(List<Player> winners) {
        winners.forEach(player -> {
            if (player != null) {
                Profile profile = getPlugin().getProfileHandler().getProfileByUniqueID(player.getUniqueId());
                if (profile != null) profile.addEventWin();
            }
        });
    }
}
