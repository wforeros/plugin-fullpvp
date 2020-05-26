package me.theoldestwilly.fullpvp.kits.menu;

import java.util.Iterator;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.kits.Kit;
import me.theoldestwilly.fullpvp.users.Profile;
import net.silexpvp.nightmare.menu.type.ChestMenu;
import net.silexpvp.nightmare.util.InventoryUtils;
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
import org.bukkit.util.ChatPaginator;

public class KitSelectorMenu extends ChestMenu<FullPvP> {
    public KitSelectorMenu(Player player) {
        super("Choose a Kit", InventoryUtils.getSafestInventorySize(FullPvP.getInstance().getKitManager().getKits().size()));
        Profile profile = plugin.getProfileHandler().getProfileByUniqueID(player.getUniqueId());
        Iterator var3 = plugin.getKitManager().getKits().iterator();

        while(var3.hasNext()) {
            Kit kit = (Kit)var3.next();
            String permission = String.format(Kit.KIT_PERMISSION_NODE, kit.getName());
            long remaining = profile.getRemainingKitCooldown(kit);
            ItemCreator creator = new ItemCreator(kit.getIcon().clone());
            creator.setDisplayName((!kit.isDisabled() && player.hasPermission(permission) && remaining <= 0L ? ChatColor.GREEN : ChatColor.RED) + kit.getName());
            creator.addLore(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "---------------------------------");
            if (kit.getDescription() != null && !kit.getDescription().isEmpty()) {
                String[] var9 = ChatPaginator.wordWrap(ChatColor.GRAY + kit.getDescription(), 24);
                int var10 = var9.length;

                for(int var11 = 0; var11 < var10; ++var11) {
                    String wrappedLine = var9[var11];
                    creator.addLore(wrappedLine);
                }

                creator.addLore("");
            }

            if (player.hasPermission(permission)) {
                if (remaining > 0L) {
                    creator.addLore(ChatColor.GRAY + "Remaining Cooldown: " + ChatColor.RED + DurationFormatUtils.formatDurationWords(remaining, true, true));
                } else {
                    creator.addLore(ChatColor.GRAY + "Remaining Cooldown: " + ChatColor.GREEN + "Ready!");
                }

                if (kit.getMaximumUses() != 2147483647) {
                    creator.addLore(ChatColor.GRAY + "Usages Remaining: " + ChatColor.WHITE + profile.getRemainingKitUsages(kit) + "/" + kit.getMaximumUses());
                }
            } else {
                creator.addLore(ChatColor.RED + "You don't have the '" + kit.getName() + "' Kit unlocked.");
                creator.addLore(ChatColor.RED + "Unlock it at " + ChatColor.GOLD + "shop.silexpvp.net" + ChatColor.RED + ".");
            }

            creator.addLore("");
            creator.addLore(ChatColor.YELLOW + "Middle-Click to preview the '" + kit.getName() + "' Kit.");
            creator.addLore(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "---------------------------------");
            this.inventory.addItem(new ItemStack[]{creator.create()});
        }

    }

    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        Inventory topInventory = event.getView().getTopInventory();
        if (topInventory.equals(this.inventory)) {
            Player player = (Player)event.getWhoClicked();
            if (topInventory.equals(clickedInventory)) {
                event.setCancelled(true);
                ItemStack item = event.getCurrentItem();
                if (item != null && item.getType() != Material.AIR && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                    Kit kit = ((FullPvP)this.plugin).getKitManager().getKit(ChatColor.stripColor(item.getItemMeta().getDisplayName()));
                    if (kit != null) {
                        if (!event.isLeftClick() && !event.isRightClick()) {
                            (new KitPreviewMenu(player, kit)).open(player);
                            player.playSound(player.getLocation(), Sound.CLICK, 1.0F, 1.0F);
                        } else if (kit.apply(player, false)) {
                            player.closeInventory();
                            player.playSound(player.getLocation(), Sound.WOOD_CLICK, 1.0F, 1.0F);
                        } else {
                            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1.0F, 0.5F);
                        }
                    }
                }
            } else if (!topInventory.equals(clickedInventory) && event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY || event.getAction() == InventoryAction.COLLECT_TO_CURSOR) {
                event.setCancelled(true);
            }

        }
    }

    public void onInventoryClose(InventoryCloseEvent event) {
    }
}
