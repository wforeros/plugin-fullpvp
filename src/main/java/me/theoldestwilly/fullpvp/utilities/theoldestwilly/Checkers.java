package me.theoldestwilly.fullpvp.utilities.theoldestwilly;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Checkers {
    public static boolean containsDifferentLetter(String string, List<Character> wantedArguments, Integer maxListValues) {
        Integer charFound = 0;
        char[] var4 = string.toCharArray();
        int var5 = var4.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            char c = var4[var6];
            if (!wantedArguments.contains(c) && !Character.isDigit(c)) {
                return true;
            }

            if (wantedArguments.contains(c)) {
                charFound = charFound + 1;
            }

            if (charFound > maxListValues) {
                return true;
            }
        }

        return false;
    }

    public static boolean isInteger(String string, Integer maxDigits) {
        Integer charFound = 0;
        if (string.length() == maxDigits) {
            char[] var3 = string.toCharArray();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                char c = var3[var5];
                if (!Character.isDigit(c)) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public static Integer getEmptySlots(ItemStack[] itemStacks) {
        Integer count = 0;
        ItemStack[] var2 = itemStacks;
        int var3 = itemStacks.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            ItemStack itemStack = var2[var4];
            if (itemStack == null) {
                count = count + 1;
            }
        }

        return count;
    }

    public static boolean isInventoryEmpty(List<ItemStack> itemStacks) {
        Iterator var1 = itemStacks.iterator();

        ItemStack itemStack;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            itemStack = (ItemStack)var1.next();
        } while(itemStack == null && itemStack.getType() == Material.AIR);

        return false;
    }

    public static List<ItemStack> removeNullItems(ItemStack[] itemStacks) {
        List<ItemStack> itemStacks1 = new ArrayList();
        ItemStack[] var2 = itemStacks;
        int var3 = itemStacks.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            ItemStack itemStack = var2[var4];
            if (itemStack != null) {
                itemStacks1.add(itemStack);
            }
        }

        return itemStacks1;
    }

    public static List<ItemStack> removeNullItems(List<ItemStack> itemStacks) {
        Iterator var1 = itemStacks.iterator();

        while(var1.hasNext()) {
            ItemStack itemStack = (ItemStack)var1.next();
            if (itemStack == null) {
                itemStacks.remove(itemStack);
            }
        }

        return itemStacks;
    }

    public static void removeAllPotionEffects(Player player) {
        Iterator var1 = player.getActivePotionEffects().iterator();

        while(var1.hasNext()) {
            PotionEffect effect = (PotionEffect)var1.next();
            player.removePotionEffect(effect.getType());
        }

    }
}
