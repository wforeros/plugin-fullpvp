package me.theoldestwilly.fullpvp.utilities.theoldestwilly;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InventoryChecker {protected ItemStack helmet;
    protected ItemStack chestplate;
    protected ItemStack leggins;
    protected ItemStack boots;
    protected ItemStack[] contents;
    protected ItemStack[] armorcontents;

    private InventoryChecker(ItemStack[] contents, ItemStack[] armorcontents, PlayerInventory inventory) {
        this.helmet = inventory.getHelmet();
        this.chestplate = inventory.getChestplate();
        this.leggins = inventory.getLeggings();
        this.boots = inventory.getBoots();
        this.contents = contents;
        this.armorcontents = armorcontents;
    }

    public InventoryChecker(PlayerInventory inventory) {
        this(inventory.getContents(), inventory.getArmorContents(), inventory);
    }

    public boolean isContCleaned() {
        ItemStack[] var1 = this.contents;
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            ItemStack itemStack = var1[var3];
            if (itemStack != null) {
                return false;
            }
        }

        return true;
    }

    public boolean isArmorCleaned() {
        ItemStack[] var1 = this.armorcontents;
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            ItemStack itemStack = var1[var3];
            if (itemStack.getType() != Material.AIR) {
                return false;
            }
        }

        return true;
    }
}
