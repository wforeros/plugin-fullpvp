package me.theoldestwilly.fullpvp.chests.mapchest;

import lombok.Getter;
import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.users.Profile;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.PluginError;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.Inventory;

public class UpdateChestMapContentsEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private @Getter
    MapChest mapChest;
    private @Getter
    Inventory inventory;
    private boolean cancelled;

    public UpdateChestMapContentsEvent(Player who, Inventory inventory) {
        super(who);
        this.mapChest = null;
        this.inventory = inventory;
        this.cancelled = false;
    }

    public void updateChestMapContents() {
        Profile profile = FullPvP.getInstance().getProfileHandler().getProfileByUniqueID(player.getUniqueId());
        if (profile != null) {
            mapChest = FullPvP.getInstance().getMapChestManager().getMapChestByName(inventory.getTitle());
            if (mapChest != null) {
                profile.updateMapChestAvailableItems(mapChest.getUniqueID(), inventory);
            } else {
                player.sendMessage(Lang.ERROR_COMMON_MESSAGE(PluginError.ERROR_LOADING_MAP_CHEST_BY_NAME));
            }
        } else {
            player.sendMessage(Lang.ERROR_LOADING_PROFILE(PluginError.ERROR_LOADING_PROFILE));
        }
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
