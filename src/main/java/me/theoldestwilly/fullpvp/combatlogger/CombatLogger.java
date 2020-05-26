package me.theoldestwilly.fullpvp.combatlogger;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class CombatLogger {
    private @Getter UUID playerUniqueId;
    private @Getter Villager entity;
    private @Getter ItemStack[] contents, armorContents;
    private @Getter String playerName;
    private @Setter Location location;
    private @Getter @Setter Long unsafeLogoutTime;
    private @Getter Long loggerDuration;

    /**
     *
     * This constructor spawns the Villager setting the inventory contents, health, potion effects...
     * @param player Player who has disconnected
     */
    public CombatLogger (Player player, Long loggerDuration) {
        playerUniqueId = player.getUniqueId();
        playerName = player.getName();
        unsafeLogoutTime = System.currentTimeMillis();
        this.loggerDuration = loggerDuration;
        PlayerInventory inventory = player.getInventory();
        contents = inventory.getContents();
        armorContents = inventory.getArmorContents();
        entity = (Villager) player.getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);
        entity.setAdult();
        location = entity.getLocation();
        Double health = player.getMaxHealth();
        entity.setMaxHealth(health * 2);
        health = player.getHealth();
        entity.setHealth(health * 2);
        entity.setCustomName(ChatColor.AQUA + playerName);
        entity.setCustomNameVisible(true);
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 99, false, false));
    }

    public boolean hasLoggerTimeEnded() {
        return loggerDuration - (System.currentTimeMillis() - unsafeLogoutTime) <= 0L;
    }

    public void removeEntity() {
        entity.remove();
    }

    public double getHealth() {
        return entity.getHealth();
    }

    public Location getLocation() {
        if (entity != null) return entity.getLocation();
        else return location;
    }

    public boolean isStillAnEntity() {
        return entity != null;
    }
}
