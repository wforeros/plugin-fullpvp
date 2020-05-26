package me.theoldestwilly.fullpvp.chests.kitschest;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.users.Profile;
import me.theoldestwilly.fullpvp.utilities.Config;
import me.theoldestwilly.fullpvp.utilities.ItemBuilder;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.PluginError;
import net.silexpvp.nightmare.util.GenericUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class KitsChestManager {
    private FullPvP plugin;
    private String alias = "kitschest";
    private Config config;
    private int[] costs;
    private List<Location> kitsChests = Collections.synchronizedList(new ArrayList<>());
    public KitsChestManager(FullPvP plugin) {
        String path = "kits-chest.cost.to-lvl-";
        this.plugin = plugin;
        //ConfigurationSerialization.registerClass(Location.class, "Locations");
        config = new Config(plugin, "kitschest");
        costs = new int[] {
                this.plugin.getConfig().getInt(path + "2"),
                this.plugin.getConfig().getInt(path + "3"),
                this.plugin.getConfig().getInt(path + "4"),
                this.plugin.getConfig().getInt(path + "5"),
                this.plugin.getConfig().getInt(path + "6"),
                this.plugin.getConfig().getInt(path + "7"),
                this.plugin.getConfig().getInt(path + "8"),
                this.plugin.getConfig().getInt(path + "9"),
                this.plugin.getConfig().getInt(path + "10")
        };
        loadKitsChest();
    }

    public void loadKitsChest() {
        Object object = this.config.get(alias);
        if (object != null && object instanceof List) {
            this.kitsChests.addAll(GenericUtils.createList(object, Location.class));
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + ChatColor.YELLOW + kitsChests.size() + " KitsChests successfully loaded.");
            reloadKitsChest(Bukkit.getConsoleSender());
        }
    }

    public void reloadKitsChest(CommandSender sender) {
        List<Location> cachedLocs = kitsChests;
        kitsChests = new ArrayList<>();
        for (Location location : cachedLocs) {
            if (location.getBlock().getType() == Material.CHEST)
                kitsChests.add(location);
        }
        Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Kits chests reloaded.", false);
    }

    public void saveKitsChest() {
        this.config.set(alias, this.kitsChests);
        this.config.save();
    }

    public void saveKitChest(Player player) {
        HashSet<Byte> set = null;
        Block block = player.getTargetBlock(set, 25);
        if (block.getType() == Material.CHEST) {
            if (!isKitChestAtLocation(block.getLocation())) {
                kitsChests.add(block.getLocation());
                player.sendMessage(Lang.SUCCESS_KITCHEST_CREATED);
            } else {
                player.sendMessage(Lang.ERROR_DB_ALREADY_CONTAINS_OBJECT);
            }
        } else {
            player.sendMessage(Lang.ERROR_NOT_LOOKING_REQUIRED_OBJECT("chest"));
        }
    }

    public void updateChestLevel(Player player) {
        Profile profile = plugin.getProfileHandler().getProfileByUniqueID(player.getUniqueId());
        if (profile != null) {
            Double cost = Double.parseDouble(getUpgradeCostByLevel(profile.getKitsChestLevel()).toString());
            if (profile.hasEnoughtMoneyToPay(cost)) {
                profile.removeMoneyFromBalance(cost);
                profile.increasKitChestLevel();
                player.sendMessage(Lang.SUCCESS_KITCHEST_UPDATE(profile.getKitsChestLevel()));
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);
            } else {
                player.sendMessage(Lang.ERROR_ECONOMY_INSUFFICENT_MONEY);
            }
        } else {
            player.sendMessage(Lang.ERROR_COMMON_MESSAGE(PluginError.ERROR_LOADING_PROFILE) + ChatColor.GRAY + " (KitsChest Manager)");
        }
    }

    public boolean isKitChestAtLocation(Location location) {
        return kitsChests.contains(location);
    }

    public void openChest(Player player, Profile profile) {
        KitsChestManager manager = FullPvP.getInstance().getKitsChestManager();
        Integer level = profile.getKitsChestLevel(), cost = manager.getUpgradeCostByLevel(level);
        Inventory inventory = manager.setInventoryContentsByLevel(profile, createInventory(level));
        if (cost != null) {
            inventory.setItem(44, (new ItemBuilder(Material.WOOL)).setDurability(13).setDisplayName("&a&lImprove the chest").setLore(Arrays.asList("", "&7Current Level: &8[&e" + level + "&7/&e10&8]", "&7Improvement Cost: &8[&e$ " + cost + "&8]")).build());
        }
        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1f, 1f);
    }

    public Inventory createInventory(int level) {
        return Bukkit.createInventory(null, 9 * 5, getDisplayName(level));
    }

    public String getDisplayName(int level) {
        return ChatColor.GRAY + "Chest " + ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "Lvl. " + level + ChatColor.DARK_GRAY + "]";
    }

    public Inventory setInventoryContentsByLevel(Profile profile, Inventory inventory) {
        int level = profile.getKitsChestLevel();
        Material boots, helmet, chestplate,
                leggings, sword;
        if (level == 6 || level == 7 || level == 8 || level == 9 || level == 10) {
            boots = Material.DIAMOND_BOOTS;
            helmet = Material.DIAMOND_HELMET;
            chestplate = Material.DIAMOND_CHESTPLATE;
            leggings = Material.DIAMOND_LEGGINGS;
        } else {
            boots = Material.IRON_BOOTS;
            helmet = Material.IRON_HELMET;
            chestplate = Material.IRON_CHESTPLATE;
            leggings = Material.IRON_LEGGINGS;
        }
        if (level == 10) sword = Material.DIAMOND_SWORD;
        else sword = Material.IRON_SWORD;
        int sharpLevel = 1, protecLevel = 2;
        ItemBuilder bootsItem = new ItemBuilder(boots),
                helmetItem = new ItemBuilder(helmet),
                chestplateItem = new ItemBuilder(chestplate),
                legginsItem = new ItemBuilder(leggings),
                swordItem = new ItemBuilder(sword);
        if (level == 10) swordItem.addEnchantment(Enchantment.DAMAGE_ALL, sharpLevel);
        if (level > 1) {
            if (level >= 2 && level < 6) bootsItem.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protecLevel);
            if (level >= 3 && level < 6) helmetItem.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protecLevel);
            if (level >= 4 && level < 6) legginsItem.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protecLevel);
            if (level >= 5 && level < 6) chestplateItem.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protecLevel);
            if (level >= 6) bootsItem.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protecLevel);
            if (level >= 7) helmetItem.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protecLevel);
            if (level >= 8) legginsItem.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protecLevel);
            if (level >= 9) chestplateItem.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, protecLevel);
            if (level == 10) swordItem.addEnchantment(Enchantment.DAMAGE_ALL, 2);
        }
        inventory.setItem(4, (helmetItem.build()));
        inventory.setItem(10, (new ItemBuilder(Material.DIAMOND_PICKAXE)).build());
        inventory.setItem(12, (new ItemBuilder(sword)).setDisplayName("&e" + profile.getName() + "'s sword").build());
        inventory.setItem(13, (chestplateItem).build());
        inventory.setItem(14, (new ItemBuilder(Material.SNOW_BALL)).setAmount(16).build());
        inventory.setItem(16, (new ItemBuilder(Material.COOKED_BEEF)).setAmount(32).build());
        inventory.setItem(21, (new ItemBuilder(Material.FISHING_ROD)).build());
        inventory.setItem(22, (legginsItem).build());
        inventory.setItem(31, (bootsItem).build());

        return inventory;
    }

    public Integer getUpgradeCostByLevel(int level) {
        Integer cost = null;
        if (level < 10) {
            if (level != 0) cost = costs[level-1];
            else cost = costs[0];
        }
        return cost;
    }
}
