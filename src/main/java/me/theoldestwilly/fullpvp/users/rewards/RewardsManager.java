package me.theoldestwilly.fullpvp.users.rewards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.utilities.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class RewardsManager {
    private FullPvP plugin;

    public RewardsManager(FullPvP plugin) {
        this.plugin = plugin;
    }

    public ItemStack createReward(RewardType type) {
        ItemStack mysteriousReward = null;
        if (type == RewardType.COMMON) {
            mysteriousReward = (new ItemBuilder(Material.SKULL_ITEM)).setDurability(3).setOwner("Chest").setDisplayName("&9Common Chest").setLore(Arrays.asList("&7Right Click to Unlock!", "&a* &7You may not get a reward!", "&a* &7Chest Rarity: &aNormal")).build();
        } else if (type == RewardType.MYSTERIOUS) {
            mysteriousReward = (new ItemBuilder(Material.SKULL_ITEM)).setDurability(3).setOwner("MHF_Present1").setDisplayName("&bLegendary Chest").setLore(Arrays.asList("&7Right Click to Unlock!", "&a* &7Guaranteed Reward!!", "&a* &7Chest Rarity: &bMysterious")).build();
        } else if (type == RewardType.LEGENDARY){
            mysteriousReward = (new ItemBuilder(Material.SKULL_ITEM)).setDurability(3).setOwner("MHF_Present2").setDisplayName("&5Mysterious Chest").setLore(Arrays.asList("&7Right Click to Unlock!", "&a* &7Guaranteed Reward!", "&a* &7This chest opens a gui", "&a* &7Chest Rarity: &6Legendary")).build();
        }
        return mysteriousReward;
    }

    public void getReward(Player player, RewardType type) {
        List<ItemStack> items = new ArrayList();
        if (type == RewardType.COMMON) {
            items.add((new ItemBuilder(Material.GOLDEN_APPLE)).setAmount(8).build());
            items.add((new ItemBuilder(Material.DIAMOND_SWORD)).addEnchantment(Enchantment.DAMAGE_ALL).build());
            items.add((new ItemBuilder(Material.DIAMOND_BLOCK)).setAmount(8).build());
            items.add((new ItemBuilder(Material.GOLDEN_APPLE)).setDurability(1).setAmount(1).build());
            Random random = new Random();
            ItemStack item = items.get(random.nextInt(items.size()));
            player.getInventory().addItem(new ItemStack[]{item});
            String itemType = item.getType().toString().toLowerCase().replace("_", " ");
            player.sendMessage(ChatColor.GREEN + "You have successfully claimed your reward and you get" + ChatColor.GRAY + ": " + ChatColor.YELLOW + itemType + ChatColor.GREEN + ".");
            player.updateInventory();
        } else {
            Inventory inventory = Bukkit.getServer().createInventory(player, 27, ChatColor.GRAY + "Reward Type: " + type.getDisplayName());
            int counter;
            int i;
            if (type == RewardType.MYSTERIOUS) {
                items.add((new ItemBuilder(Material.GOLDEN_APPLE)).setAmount(16).build());
                items.add((new ItemBuilder(Material.DIAMOND_SWORD)).addEnchantment(Enchantment.DAMAGE_ALL, 3).build());
                items.add((new ItemBuilder(Material.DIAMOND_BLOCK)).setAmount(16).build());
                items.add((new ItemBuilder(Material.GOLDEN_APPLE)).setDurability(1).setAmount(4).build());
                items.add((new ItemBuilder(Material.DIAMOND_CHESTPLATE)).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3).addEnchantment(Enchantment.DURABILITY, 2).build());
                counter = 0;

                for(i = 11; i < items.size() + 11; ++i) {
                    inventory.setItem(i, (ItemStack)items.get(counter));
                    ++counter;
                }
            } else if (type == RewardType.LEGENDARY) {
                items.add((new ItemBuilder(Material.GOLDEN_APPLE)).setAmount(32).build());
                items.add((new ItemBuilder(Material.DIAMOND_AXE)).addEnchantment(Enchantment.DAMAGE_ALL, 6).addEnchantment(Enchantment.DURABILITY, 3).addEnchantment(Enchantment.FIRE_ASPECT, 1).addEnchantment(Enchantment.DIG_SPEED, 5).build());
                items.add((new ItemBuilder(Material.DIAMOND_HELMET)).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5).addEnchantment(Enchantment.DURABILITY, 4).build());
                items.add((new ItemBuilder(Material.DIAMOND_BOOTS)).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5).addEnchantment(Enchantment.DURABILITY, 4).build());
                items.add((new ItemBuilder(Material.GOLDEN_APPLE)).setDurability(1).setAmount(8).build());
                counter = 0;

                for(i = 11; i < items.size() + 11; ++i) {
                    inventory.setItem(i, (items.get(counter)));
                    ++counter;
                }
            }
            player.openInventory(inventory);
            player.sendMessage(ChatColor.BLUE + "You have successfully claimed your reward!");
        }
    }
}

