package me.theoldestwilly.fullpvp.crate;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.utilities.chat.ClickAction;
import me.theoldestwilly.fullpvp.utilities.chat.Text;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.Checkers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class CrateListener implements Listener {
    private FullPvP plugin;

    public CrateListener(FullPvP plugin) {
        this.plugin = plugin;
    }

    public Inventory getCreateRewards(Player player, Crate crate) {
        Inventory inventory = Bukkit.getServer().createInventory(player, 9, ChatColor.BLUE + "Crate: Rewards");

        for(int i = 0; i < crate.getRewards(); ++i) {
            inventory.addItem(new ItemStack[]{(ItemStack)crate.getItems().get((new Random()).nextInt(crate.getItems().size()))});
        }

        return inventory;
    }

    @EventHandler(
            priority = EventPriority.HIGH,
            ignoreCancelled = true
    )
    public void onOpenCrate(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.ENDER_CHEST) {
            Player player = event.getPlayer();
            if (event.getPlayer().getItemInHand().getType() == Material.TRIPWIRE_HOOK && event.getClickedBlock() != null && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Crate crate = this.plugin.getCrateManager().getCrateByItemStack(player.getItemInHand());
                Text text = new Text();
                event.setCancelled(true);
                if (crate == null) {
                    player.sendMessage(ChatColor.RED + "Invalid key.");
                    return;
                }

                if (crate.getItems().size() == 0) {
                    player.sendMessage(ChatColor.RED + "No items found for this key, please contact an administrator.");
                    return;
                }

                Inventory rewards = this.getCreateRewards(player, crate);
                if (crate.hasBroadcast()) {
                    text.append((new Text("The player ")).setColor(ChatColor.YELLOW));
                    text.append((new Text(player.getName())).setColor(ChatColor.WHITE));
                    text.append((new Text(" has opened a ")).setColor(ChatColor.YELLOW));
                    text.append((new Text("[")).setColor(ChatColor.GRAY));
                    text.append((new Text(crate.getName() + " key")).setColor(crate.getColor()).setBold(true).setHoverText(ChatColor.YELLOW + "Click to see key rewards").setClick(ClickAction.RUN_COMMAND, "/crate gui " + crate.getName()));
                    text.append((new Text("]")).setColor(ChatColor.GRAY));
                    text.append((new Text(".")).setColor(ChatColor.YELLOW));
                    Iterator var6 = Bukkit.getServer().getOnlinePlayers().iterator();

                    while(var6.hasNext()) {
                        Player player1 = (Player)var6.next();
                        text.send(player1);
                    }
                }

                ItemStack held = player.getItemInHand();
                if (held.getAmount() > 1) {
                    held.setAmount(held.getAmount() - 1);
                } else {
                    player.setItemInHand(new ItemStack(Material.AIR));
                }

                player.openInventory(rewards);
            } else if (event.getAction() == Action.LEFT_CLICK_BLOCK && player.getGameMode() != GameMode.CREATIVE) {
                player.openInventory(this.plugin.getCrateManager().openCratesGui(player));
            }
        }

    }

    @EventHandler
    public void onCloseCrate(InventoryCloseEvent event) {
        if (event.getInventory().getTitle().equals(ChatColor.BLUE + "Crate: Rewards")) {
            Inventory inventory = event.getInventory();
            List<ItemStack> items = new ArrayList();
            Player player = (Player)event.getPlayer();
            ItemStack[] var5 = inventory.getContents();
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                ItemStack itemStack = var5[var7];
                if (itemStack != null) {
                    items.add(itemStack);
                }
            }

            if (inventory.getContents().length - Checkers.getEmptySlots(inventory.getContents()) != 0) {
                if (Checkers.getEmptySlots(player.getInventory().getContents()) < inventory.getContents().length - Checkers.getEmptySlots(inventory.getContents())) {
                    player.sendMessage(ChatColor.RED + "You do not have space in your inventory so the items have been dropped.");
                    Iterator var9 = items.iterator();

                    while(var9.hasNext()) {
                        ItemStack itemStack = (ItemStack)var9.next();
                        if (itemStack != null) {
                            player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
                        }
                    }

                    return;
                }

                player.getInventory().addItem((ItemStack[])items.toArray(new ItemStack[0]));
                player.sendMessage(ChatColor.BLUE + "You have closed the inventory with rewards so these have been sent to your inventory");
            }
        }

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory() != null && event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
            if (event.getInventory().getTitle().equals(ChatColor.BLUE + "Crates: Rewards")) {
                event.setCancelled(true);
                Player player = (Player)event.getWhoClicked();
                player.openInventory(this.plugin.getCrateManager().openCrateLoot(player, event.getCurrentItem()));
            } else if (event.getInventory().getTitle().contains(" key rewards")) {
                event.setCancelled(true);
            }
        }

    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.HIGH
    )
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getItemInHand().getType() == Material.TRIPWIRE_HOOK && event.getItemInHand().getItemMeta() != null) {
            Crate crate = this.plugin.getCrateManager().getCrateByItemStack(event.getItemInHand());
            if (crate != null) {
                event.setCancelled(true);
            }
        }

    }
}
