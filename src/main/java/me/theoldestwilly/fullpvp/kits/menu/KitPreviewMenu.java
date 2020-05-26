package me.theoldestwilly.fullpvp.kits.menu;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.kits.Kit;
import me.theoldestwilly.fullpvp.users.Profile;
import net.silexpvp.nightmare.menu.type.ChestMenu;
import net.silexpvp.nightmare.util.ItemCreator;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class KitPreviewMenu extends ChestMenu<FullPvP> {
    public KitPreviewMenu(Player player, Kit kit) {
        super(kit.getName() + " Preview", 54);
        Profile profile = plugin.getProfileHandler().getProfileByUniqueID(player.getUniqueId());
        String permission = String.format(Kit.KIT_PERMISSION_NODE, kit.getName());
        long remaining = profile.getRemainingKitCooldown(kit);
        this.inventory.setItem(0, (new ItemCreator(Material.DIODE)).setDisplayName("&cBack").addLore("&7Click to go back.").create());
        this.inventory.setItem(4, (new ItemCreator(kit.getIcon().clone())).setDisplayName((!kit.isDisabled() && player.hasPermission(permission) && remaining <= 0L ? ChatColor.GREEN : ChatColor.RED) + kit.getName()).addLore("&7Cooldown per apply: &f" + (kit.getCooldown() > 0L ? DurationFormatUtils.formatDurationWords(kit.getCooldown(), true, true) : "None")).addLore("&7Maximum uses: &f" + (kit.getMaximumUses() == 2147483647 ? "Unlimited (" + profile.getRemainingKitUsages(kit) + " times applied)" : profile.getRemainingKitUsages(kit) + "/" + kit.getMaximumUses())).create());
        int slot = 8;
        ItemStack[] var8 = kit.getContents();
        int var9 = var8.length;

        for(int var10 = 0; var10 < var9; ++var10) {
            ItemStack content = var8[var10];
            ++slot;
            if (content != null && content.getType() != Material.AIR) {
                this.inventory.setItem(slot, content.clone());
            }
        }

        this.inventory.setItem(45, kit.getArmour()[3].clone());
        this.inventory.setItem(46, kit.getArmour()[2].clone());
        this.inventory.setItem(47, kit.getArmour()[1].clone());
        this.inventory.setItem(48, kit.getArmour()[0].clone());
    }

    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        Inventory topInventory = event.getView().getTopInventory();
        if (topInventory.equals(this.inventory)) {
            Player player = (Player)event.getWhoClicked();
            if (topInventory.equals(clickedInventory)) {
                event.setCancelled(true);
                ItemStack item = event.getCurrentItem();
                if (item != null && item.getType() != Material.AIR && event.getRawSlot() == 0) {
                    (new KitSelectorMenu(player)).open(player);
                    player.playSound(player.getLocation(), Sound.CLICK, 1.0F, 1.0F);
                }
            } else if (!topInventory.equals(clickedInventory) && event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY || event.getAction() == InventoryAction.COLLECT_TO_CURSOR) {
                event.setCancelled(true);
            }

        }
    }

    public void onInventoryClose(InventoryCloseEvent event) {
    }
}
