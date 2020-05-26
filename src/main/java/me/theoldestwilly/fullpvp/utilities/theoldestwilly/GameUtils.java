package me.theoldestwilly.fullpvp.utilities.theoldestwilly;

import me.theoldestwilly.fullpvp.chests.mapchest.ChestItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Chest;

import java.util.*;
import java.util.stream.Stream;

public class GameUtils {

    public static Player getPlayerDamagerFromEntity(Entity entity) {
        if (entity instanceof Player) return (Player) entity;
        if (entity instanceof Projectile) {
            Projectile projectile = (Projectile) entity;
            if (projectile.getShooter() instanceof Player)
                return (Player) projectile.getShooter();
        }
        return null;
    }

    public static ChestItem[] convertInventoryToChestItemsArray(Inventory inventory) {
        List<ChestItem> list = new ArrayList<>();
        int i = 0;
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null && itemStack.getType() != Material.AIR)
                list.add(new ChestItem(i, itemStack));
            i++;
        }
        return list.toArray(new ChestItem[0]);
    }

    public static ChestItem[] convertContentsToChestItemsArray(ItemStack[] contents) {
        List<ChestItem> list = new ArrayList<>();
        int i = 0;
        for (ItemStack itemStack : contents) {
            if (itemStack != null && itemStack.getType() != Material.AIR)
                list.add(new ChestItem(i, itemStack));
            i++;
        }
        return list.toArray(new ChestItem[0]);
    }

    public static Inventory setChestItemsIntoInventory(ChestItem[] chestItems, Inventory inventory) {
        Stream.of(chestItems).forEach(chestItem -> inventory.setItem(chestItem.getSlot(), chestItem.getItemStack()));
        return inventory;
    }

    public static String[] serializeChestItemsArray(ChestItem[] chestItems) {
        List<String> list = new ArrayList<>();
        for (ChestItem chestItem : chestItems) {
            list.add(chestItem.toString());
        }
        return list.toArray(new String[0]);
    }

    public static void clearChat (Collection<? extends Player> collection) {
        collection.forEach(player -> {
            if (player != null) clearChat(player);
        });
    }

    public static void clearChat(Player player) {
        for (int i = 0; i < 30; i++) player.sendMessage("");
    }

    public static void hidePlayer(Player player, Player player1) {
        hidePlayers(player, Collections.singleton(player1));
    }

    public static void hidePlayers(Player player, Iterable<? extends Player> players) {
       players.forEach(player1 -> {
           if (player1 != null) player.hidePlayer(player1);
       });
    }

    public static void hidePlayer(Iterable<? extends Player> players, Player player) {
        players.forEach(player1 -> {
            if (player1 != null) player1.hidePlayer(player);
        });
    }

    public static void showPlayer(Iterable<? extends Player> players, Player player) {
        players.forEach(player1 -> {
            if (player1 != null) player1.showPlayer(player);
        });
    }

    public static void showPlayer(Player player, Player player1) {
        showPlayers(player, Collections.singleton(player1));
    }

    public static void showPlayers(Player player, Iterable<? extends Player> players) {
        players.forEach(player1 -> {
            if (player1 != null) player.showPlayer(player1);
        });
    }

    public static Double getKills(int rankupKills, Integer level) {
        return rankupKills * Math.pow(level, 1.25D);
    }
}
