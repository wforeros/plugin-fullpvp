package me.theoldestwilly.fullpvp.chests.kitschest;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.users.Profile;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.PluginError;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class KitsChestListener implements Listener {

    @EventHandler
    public void onPlayerInteractEvent (PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.CHEST) {
            KitsChestManager manager = FullPvP.getInstance().getKitsChestManager();
            if (manager.isKitChestAtLocation(event.getClickedBlock().getLocation())) {
                event.setCancelled(true);
                Player player = event.getPlayer();
                Profile profile = FullPvP.getInstance().getProfileHandler().getProfileByUniqueID(player.getUniqueId());
                if (profile != null) {
                    manager.openChest(player, profile);
                } else {
                    player.sendMessage(Lang.ERROR_LOADING_PROFILE(PluginError.ERROR_LOADING_PROFILE) + ChatColor.GRAY + "(KitsChest Listener)");
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (event.getInventory() != null && event.getCurrentItem() != null &&
                ChatColor.stripColor(event.getInventory().getTitle()).contains("Chest [Lvl.") &&
                event.getRawSlot() == 44 && event.getInventory().getItem(44) != null) {
            Player player = (Player)event.getWhoClicked();
            event.setCancelled(true);
            FullPvP.getInstance().getKitsChestManager().updateChestLevel(player);
            player.closeInventory();
        }

    }
}
