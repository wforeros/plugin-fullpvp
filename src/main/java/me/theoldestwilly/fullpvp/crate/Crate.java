package me.theoldestwilly.fullpvp.crate;

import me.theoldestwilly.fullpvp.utilities.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Crate implements ConfigurationSerializable {
    private Integer rewards;
    private String name;
    private Boolean broadcast;
    private ChatColor color;
    private List<ItemStack> items;

    public Crate(String name, Integer rewards, String color, Object items, Boolean broadcast) {
        this.items = new ArrayList();
        this.name = name;
        this.rewards = rewards;
        this.color = ChatColor.valueOf(color);
        if (items instanceof List) {
            this.items.addAll((List)items);
        } else {
            this.items = null;
        }

        this.broadcast = broadcast;
    }

    public Crate(Map<String, Object> map) {
        this((String)map.get("name"), (Integer)map.get("rewards"), (String)map.get("color"), map.get("items"), (Boolean)map.get("broadcast"));
    }

    public Crate(String name, Integer rewards, ItemStack[] itemStacks) {
        this.items = new ArrayList();
        this.name = name;
        this.rewards = rewards;
        this.broadcast = true;
        this.setRewards(itemStacks);
        this.color = ChatColor.BLUE;
    }

    public Boolean hasBroadcast() {
        return this.broadcast;
    }

    public void setRewardsNumber(Integer number) {
        this.rewards = this.rewards;
    }

    public ItemStack giveKeys(Integer ammount) {
        return (new ItemBuilder(Material.TRIPWIRE_HOOK)).setDisplayName(this.color + this.name + " Key").setAmount(ammount).setLore(Arrays.asList("&7Right click an ender chest", "&7to claim your reward!")).build();
    }

    public void setRewards(ItemStack[] itemStacks) {
        List<ItemStack> items = new ArrayList();
        ItemStack[] var3 = itemStacks;
        int var4 = itemStacks.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            ItemStack itemStack = var3[var5];
            if (itemStack != null) {
                items.add(itemStack);
            }
        }

        this.items = items;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap();
        map.put("name", this.name);
        map.put("rewards", this.rewards);
        map.put("broadcast", this.broadcast);
        map.put("color", this.color.name());
        map.put("items", this.items);
        return map;
    }

    public Integer getRewards() {
        return this.rewards;
    }

    public String getName() {
        return this.name;
    }

    public ChatColor getColor() {
        return this.color;
    }

    public List<ItemStack> getItems() {
        return this.items;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBroadcast(Boolean broadcast) {
        this.broadcast = broadcast;
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }

    public Boolean getBroadcast() {
        return this.broadcast;
    }
}
