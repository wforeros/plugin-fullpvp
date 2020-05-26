package me.theoldestwilly.fullpvp.utilities;


import com.google.common.base.Preconditions;
import me.theoldestwilly.fullpvp.utilities.chat.TextUtils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemBuilder {
    private ItemStack itemStack;

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
    }

    public ItemBuilder(ItemStack itemStack) {
        Preconditions.checkNotNull(itemStack, "ItemStack cannot be null");
        this.itemStack = itemStack;
    }

    public ItemBuilder setDisplayName(String displayName) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.setDisplayName(TextUtils.formatColor(displayName));
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder addLore(String input) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        List<String> lores = itemMeta.getLore();
        if (lores == null) {
            lores = new ArrayList();
        }

        ((List)lores).add(TextUtils.formatColor(input));
        itemMeta.setLore((List)lores);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setLore(List<String> input) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.setLore(TextUtils.formatColor(input));
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder clearLore() {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.setLore((List)null);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setDurability(int durability) {
        this.itemStack.setDurability((short)durability);
        return this;
    }

    public ItemBuilder setData(int data) {
        this.itemStack.setData(new MaterialData(this.itemStack.getType(), (byte)data));
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        this.itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment) {
        this.itemStack.addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    public ItemBuilder setMaterial(Material material) {
        this.itemStack.setType(material);
        return this;
    }

    public ItemBuilder setOwner(String owner) {
        SkullMeta skullMeta = (SkullMeta)this.itemStack.getItemMeta();
        skullMeta.setOwner(owner);
        this.itemStack.setItemMeta(skullMeta);
        return this;
    }

    public ItemBuilder clearEnchantments() {
        Iterator var1 = this.itemStack.getEnchantments().keySet().iterator();

        while(var1.hasNext()) {
            Enchantment enchantments = (Enchantment)var1.next();
            this.itemStack.removeEnchantment(enchantments);
        }

        return this;
    }

    public ItemBuilder setColor(Color color) {
        if (this.itemStack.getType() != Material.LEATHER_HELMET && this.itemStack.getType() != Material.LEATHER_CHESTPLATE && this.itemStack.getType() != Material.LEATHER_LEGGINGS && this.itemStack.getType() != Material.LEATHER_BOOTS) {
            throw new IllegalArgumentException("You can only apply color to leather armor!");
        } else {
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta)this.itemStack.getItemMeta();
            leatherArmorMeta.setColor(color);
            this.itemStack.setItemMeta(leatherArmorMeta);
            return this;
        }
    }

    public ItemStack build() {
        return this.itemStack;
    }
}

