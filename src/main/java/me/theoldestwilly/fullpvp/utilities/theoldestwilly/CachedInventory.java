package me.theoldestwilly.fullpvp.utilities.theoldestwilly;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class CachedInventory {
    protected ItemStack[] armorContents;
    protected ItemStack[] contents;

    private CachedInventory(ItemStack[] contents, ItemStack[] armorContents) {
        this.armorContents = armorContents;
        this.contents = contents;
    }

    public CachedInventory(PlayerInventory playerInventory) {
        this(playerInventory.getContents(), playerInventory.getArmorContents());
    }

    public ItemStack[] getArmorContents() {
        return this.armorContents;
    }

    public ItemStack[] getContents() {
        return this.contents;
    }
}

