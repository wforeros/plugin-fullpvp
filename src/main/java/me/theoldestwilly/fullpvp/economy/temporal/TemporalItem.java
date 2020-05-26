package me.theoldestwilly.fullpvp.economy.temporal;

import lombok.Getter;
import me.theoldestwilly.fullpvp.utilities.ItemBuilder;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.UUID;

@Getter
public class TemporalItem {
    private ItemStack item;
    private double cost;
    private Long duration;
    private Rarity rarity;
    private UUID uniqiueId;

    public TemporalItem(ItemStack item, double cost, Long duration, Rarity rarity, UUID uniqiueId) {
        this.cost = cost;
        this.duration = duration;
        this.rarity = rarity != null ? rarity:Rarity.COMMON;
        this.uniqiueId = uniqiueId != null ? uniqiueId : UUID.randomUUID();
        createItem(item);
    }

    public TemporalItem(ItemStack item, double cost, Long duration, Rarity rarity) {
        this(item, cost, duration, rarity, null);
    }

    public void createItem(ItemStack item) {
        item = new ItemBuilder(item).setLore(Arrays.asList(
                "&7&m-------------------",
                "&fThis is an exclusive and temporal item!",
                "&7Bought by: &f" + cost,
                "&7Rarity: &f" + rarity,
                "&7&m-------------------")
        ).build();
    }

    public ItemStack getItemStackForSale() {
        return new ItemBuilder(item).setLore(Arrays.asList(
                "&7&m-------------------",
                "&fThis is an exclusive and temporal item!",
                "&7Cost: &f" + cost,
                "&7Rarity: &f" + rarity,
                "&7&m-------------------")
        ).build();
    }
}
