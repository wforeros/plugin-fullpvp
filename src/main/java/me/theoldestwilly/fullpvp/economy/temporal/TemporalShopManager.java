package me.theoldestwilly.fullpvp.economy.temporal;

import me.theoldestwilly.fullpvp.FullPvP;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TemporalShopManager {
    private FullPvP plugin;
    private List<TemporalItem> items = new ArrayList<>();
    public TemporalShopManager(FullPvP plugin) {
        this.plugin = plugin;
    }

    public void addTemporalItem(Double cost, Long duration, ItemStack item, Rarity rarity) {
        items.add(new TemporalItem(item, cost, duration, rarity));
    }

    public void removeTamporalItem (UUID uniqueId) {
        TemporalItem item = getItem(uniqueId);
        if (item != null) items.remove(item);
    }

    public TemporalItem getItem (UUID uniqueId) {
        return items.stream().filter(item -> item.getUniqiueId().equals(uniqueId)).findFirst().orElse(null);
    }

    public void openShopInventory(Player player) {
        int inventoryLines = (int) Math.ceil((items.size() / 9.0));
        Inventory inventory = Bukkit.createInventory(player, inventoryLines * 9, ChatColor.GOLD + ChatColor.BOLD.toString() + "Temporal Shop");

    }
}
