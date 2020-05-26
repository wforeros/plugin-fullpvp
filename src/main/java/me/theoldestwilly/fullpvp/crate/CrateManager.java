package me.theoldestwilly.fullpvp.crate;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.chests.mapchest.MapChest;
import me.theoldestwilly.fullpvp.utilities.Config;
import me.theoldestwilly.fullpvp.utilities.ItemBuilder;
import net.silexpvp.nightmare.util.GenericUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.*;

public class CrateManager {
    private FullPvP plugin;
    private List<Crate> cratesList = new ArrayList<>();
    private Config config;
    private String alias = "crates";

    public CrateManager(FullPvP plugin) {
        this.plugin = plugin;
        ConfigurationSerialization.registerClass(Crate.class, "Crate");
        config = new Config(plugin, "crates");
        loadCrates();
    }

    public void loadCrates() {
        Object object = this.config.get(alias);
        String msg;
        if (object != null && object instanceof List) {
            this.cratesList.addAll(GenericUtils.createList(object, Crate.class));
            if (!this.cratesList.isEmpty())
                msg = Lang.PREFIX + ChatColor.YELLOW + this.cratesList.size() + " Crates successfully loaded.";
            else
                msg = Lang.PREFIX + Lang.ERROR_COLOR + "There are not crates registered into 'crates.yml'.";
        } else msg = Lang.PREFIX + Lang.ERROR_COLOR + ChatColor.BOLD + "Error: " + ChatColor.RED + "'" + alias + ".yml' not found.";
        Bukkit.getConsoleSender().sendMessage(msg);
    }

    public void saveCrates() {
        this.config.set(alias, this.cratesList);
        this.config.save();
    }

    public void onCrateCreate(Player player, String name, Integer rewards) {
        if (rewards <= 9 && rewards >= 1) {
            if (this.getCrateByName(name) != null) {
                player.sendMessage(ChatColor.RED + "This crate already exists, try with another name.");
            } else if (player.getInventory().getContents().length == 0) {
                player.sendMessage(ChatColor.RED + "The content of your inventory can not be used to create the key reward.");
            } else {
                Crate crate = new Crate(name, rewards, player.getInventory().getContents());
                this.getCratesList().add(crate);
                player.sendMessage(ChatColor.GREEN + "You have created the key " + ChatColor.AQUA + crate.getName() + ChatColor.GREEN + " with " + ChatColor.YELLOW + crate.getRewards() + ChatColor.GREEN + " rewards.");
            }
        } else {
            player.sendMessage(ChatColor.RED + "The number entered must be between 1 and 9.");
        }
    }

    public void onCrateDelete(Player player, String name) {
        Crate crate = this.getCrateByName(name);
        if (crate != null) {
            this.getCratesList().remove(crate);
            player.sendMessage(ChatColor.BLUE + "The key " + ChatColor.GRAY + name + ChatColor.BLUE + " have been eliminated.");
        } else {
            player.sendMessage(ChatColor.RED + "Key with name '" + ChatColor.GRAY + name + ChatColor.RED + "' not found.");
        }

    }

    public Crate getCrateByName(String name) {
        name = ChatColor.stripColor(name);
        for (Crate crate : cratesList) {
            if (crate.getName().equalsIgnoreCase(name))
                return crate;
        }

        return null;
    }

    public Crate getCrateByItemStack(ItemStack itemStack) {
        if (itemStack != null) {
            String[] name = itemStack.getItemMeta().getDisplayName().split(" ");
            return this.getCrateByName(ChatColor.stripColor(name[0]));
        } else {
            return null;
        }
    }

    public Inventory openCratesGui(Player player) {
        int inventorySize = (int) Math.ceil((cratesList.size() / 9.0));
        Inventory inventory = Bukkit.getServer().createInventory(player, 9 * inventorySize, ChatColor.BLUE + "Crates: Rewards");
        Iterator var4 = this.getCratesList().iterator();

        while(var4.hasNext()) {
            Crate crate = (Crate)var4.next();
            inventory.addItem(new ItemStack[]{(new ItemBuilder(Material.CHEST)).setDisplayName(crate.getColor() + crate.getName() + " key").setLore(Arrays.asList("&7Click to see the key rewards")).build()});
        }

        return inventory;
    }

    public Inventory openCrateLoot(Player player, ItemStack itemStack) {
        if (itemStack != null) {
            Crate crate = this.getCrateByItemStack(itemStack);
            int inventoryLines = (int) Math.ceil((crate.getItems().size() / 9.0));
            Inventory inventory = Bukkit.getServer().createInventory(player, 9 * inventoryLines, ChatColor.YELLOW + "" + crate.getColor() + crate.getName() + " key rewards" + ChatColor.YELLOW + "");
            inventory.addItem((ItemStack[])crate.getItems().toArray(new ItemStack[0]));
            return inventory;
        } else {
            return null;
        }
    }

    public Inventory openCrateLoot(Player player, String name) {
        Crate crate = this.getCrateByName(name);
        int inventoryLines = (int) Math.ceil((crate.getItems().size() / 9.0));
        Inventory inventory = Bukkit.getServer().createInventory(player, 9 * inventoryLines, ChatColor.YELLOW + "" + crate.getColor() + crate.getName() + " key rewards" + ChatColor.YELLOW + "");
        inventory.addItem((ItemStack[])crate.getItems().toArray(new ItemStack[0]));
        return inventory;
    }

    public List<Crate> getCratesList() {
        return this.cratesList;
    }
}
