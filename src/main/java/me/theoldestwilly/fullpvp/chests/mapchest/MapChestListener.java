package me.theoldestwilly.fullpvp.chests.mapchest;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.users.Profile;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.PluginError;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;

public class MapChestListener implements Listener {

    @EventHandler
    public void onPlayerInteractEvent (PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.CHEST) {
            MapChest mapChest = FullPvP.getInstance().getMapChestManager().getMapChestByLocation(event.getClickedBlock().getLocation());
            if (mapChest != null) {
                event.setCancelled(true);
                Player player = event.getPlayer();
                Profile profile = FullPvP.getInstance().getProfileHandler().getProfileByUniqueID(player.getUniqueId());
                if (profile != null) {
                    Long delay = profile.getMapChestRemainingDelay(mapChest.getUniqueID());
                    boolean hasDelay = delay != null && mapChest.getDelay() > delay;
                    mapChest.openChest(player, profile, hasDelay);
                    if (delay != null && hasDelay)
                        player.sendMessage(Lang.WARNING_REMAINING_TIME_MAP_CHEST(mapChest.getDelay() - delay));
                } else {
                    player.sendMessage(Lang.ERROR_LOADING_PROFILE(PluginError.ERROR_LOADING_PROFILE) + ChatColor.GRAY + "(MapChest Listener)");
                }

            }
        }
    }

    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent event) {
        if (event.getInventory() != null &&
                ChatColor.stripColor(event.getInventory().getTitle()).contains("Chest #")) {
            Player player = (Player) event.getPlayer();
            player.playSound(player.getLocation(), Sound.CHEST_CLOSE, 1f, 1f);
        }
    }
}
