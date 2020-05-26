package me.theoldestwilly.fullpvp.event.hosted;

import lombok.Getter;
import lombok.Setter;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.event.hosted.structure.HostedEventType;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.CachedInventory;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.PluginError;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public class Participant {
    private UUID uniqueId;
    private String name;
    private String displayName;
    private CachedInventory cachedInventory;
    private HostedEventType currentHostedEventType;
    private @Setter ParticipantRoll roll;
    private @Setter ParticipantRoll.SubRoll subRoll;
    private @Setter boolean alive;
    private Boolean allowFlight;
    //Roll

    public Participant (Player player, HostedEventType type, ParticipantRoll roll) {
        name = player.getName();
        uniqueId = player.getUniqueId();
        displayName = player.getDisplayName();
        cachedInventory = new CachedInventory(player.getInventory());
        currentHostedEventType = type;
        this.roll = roll;
        alive = true;
        allowFlight = player.getAllowFlight();
    }

    public void returnCachedInventory() {
        Player player = getPlayer();
        if (getCachedInventory() != null) {
            player.getInventory().setArmorContents(null);
            player.getInventory().clear();
            player.getInventory().setContents(cachedInventory.getContents());
            player.getInventory().setArmorContents(cachedInventory.getArmorContents());
            player.updateInventory();
        } else player.sendMessage(Lang.ERROR_COMMON_MESSAGE(PluginError.ERROR_LOADING_EVENT_CACHED_INVENTORY));
    }

    public boolean isSpectator() {
        if (roll == ParticipantRoll.SPECTATOR) return true;
        return false;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uniqueId);
    }
}
