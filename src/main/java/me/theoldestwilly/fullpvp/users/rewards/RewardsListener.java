package me.theoldestwilly.fullpvp.users.rewards;

import java.util.List;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.users.Profile;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class RewardsListener implements Listener {
    private FullPvP plugin;

    public RewardsListener(FullPvP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (event.getInventory() != null) {
            Inventory inventory = event.getInventory();
            String title = inventory.getTitle();
            Player player;
            if (title.contains("MHF_Present")) {
                player = (Player)event.getWhoClicked();
                int slot = event.getRawSlot();
                if (event.getWhoClicked() != null && event.getWhoClicked() instanceof Player && event.getCurrentItem() != null && event.getSlotType() == SlotType.CONTAINER && slot <= 15 && slot >= 11) {
                    ItemStack itemStack = event.getCurrentItem();
                    player.getInventory().addItem(new ItemStack[]{itemStack});
                    player.closeInventory();
                    player.updateInventory();
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.YELLOW + "You have successfully claimed your reward, to get more you must increase your killstreak!");
                }
            } else if (title.contains("Rewards GUI")) {
                player = (Player)event.getWhoClicked();
                Profile profile = this.plugin.getProfileHandler().getProfileByUniqueID(player.getUniqueId());
                List<ItemStack> rewards = profile.getRewards();
                int slot = event.getRawSlot();
                if (rewards != null && !rewards.isEmpty() && slot == 8) {
                    Inventory inve = player.getInventory();
                    inve.addItem((ItemStack[])rewards.toArray(new ItemStack[0]));
                    profile.resetRewards();
                    player.closeInventory();
                    player.updateInventory();
                }

                if (slot <= 8) {
                    event.setCancelled(true);
                }
            }
        }

    }

    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent event) {
        if (event.getInventory() != null) {
            Inventory inventory = event.getInventory();
            String title = inventory.getTitle();
            if (title.contains("Reward Type: ") && event.getPlayer() instanceof Player) {
                Player player = (Player)event.getPlayer();
                player.updateInventory();
            }
        }
    }

    @EventHandler
    public void onPlaceRewardSkull(PlayerInteractEvent event) {
        ItemStack itemStack = event.getItem();
        if (itemStack != null && itemStack.getType() == Material.SKULL_ITEM && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) {
            SkullMeta meta = (SkullMeta)event.getItem().getItemMeta();
            String owner = meta.getOwner();
            if (owner != null && (owner.contains("MHF_Present") || owner.contains("Chest"))) {
                Player player = event.getPlayer();
                this.plugin.getRewardsManager().getReward(player, RewardType.getRewardBySkullOwner(owner));
                itemStack.setAmount(itemStack.getAmount() - 1);
                player.setItemInHand(itemStack);
            }
        }

    }
}

