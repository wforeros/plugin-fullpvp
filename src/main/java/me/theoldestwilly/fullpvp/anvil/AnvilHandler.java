package me.theoldestwilly.fullpvp.anvil;


import lombok.Getter;
import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.utilities.Config;
import net.silexpvp.nightmare.util.GenericUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.logging.Level;

public class AnvilHandler implements Listener {
    private FullPvP plugin;
    private @Getter
    Config config;
    private List<Location> anvilLocations = new ArrayList();
    private YamlConfiguration anvilListYml = new YamlConfiguration();
    private Set set;
    private String alias = "anvils";

    public AnvilHandler(FullPvP plugin) {
        this.plugin = plugin;
        config = new Config(plugin, alias);
        ConfigurationSerialization.registerClass(Location.class, "Locations");
        this.loadAnvils();
        this.replaceAnvilTask();
    }

    public void loadAnvils() {
        Object object = this.config.get(alias);
        if (object != null && object instanceof List) {
            this.anvilLocations.addAll(GenericUtils.createList(object, Location.class));
            Bukkit.getLogger().log(Level.INFO, "[FullPvP] Anvils successfully loaded " + this.anvilLocations.size() + " anvil" + (this.anvilLocations.size() == 0 ? "" : "s") + ".");
        }
    }

    public void saveAnvils() {
        this.config.set(alias, this.anvilLocations);
        this.config.save();
    }

    public void saveLocation(Player player) {
        HashSet<Byte> set = null;
        Block block = player.getTargetBlock(set, 25);
        if (block.getType() == Material.ANVIL) {
            Location location = block.getLocation();
            if (!this.getAnvilLocations().contains(location)) {
                this.getAnvilLocations().add(block.getLocation());
                player.sendMessage(ChatColor.GREEN + "The anvil has been saved successfully.");
            } else {
                player.sendMessage(ChatColor.YELLOW + "This anvil is already in the database.");
            }
        } else {
            player.sendMessage(ChatColor.RED + "You are not looking at an anvil.");
        }

    }

    public void removeLocation(Player player) {
        HashSet<Byte> set = null;
        Block block = player.getTargetBlock(set, 50);
        if (block.getType() == Material.ANVIL) {
            Location location = block.getLocation();
            if (this.getAnvilLocations().contains(location)) {
                this.getAnvilLocations().remove(block.getLocation());
                player.sendMessage(ChatColor.GREEN + "The anvil has been successfully deleted.");
                if (this.getAnvilLocations().size() == 1) {
                    this.getAnvilLocations().clear();
                }
            } else {
                player.sendMessage(ChatColor.YELLOW + "This anvil is not found in the database.");
            }
        } else {
            player.sendMessage(ChatColor.RED + "You are not looking at an anvil.");
        }

    }

    public void replaceAnvilTask() {
        (new BukkitRunnable() {
            public void run() {
                if (AnvilHandler.this.getAnvilLocations().size() != 0) {
                    Iterator var1 = AnvilHandler.this.getAnvilLocations().iterator();

                    while(var1.hasNext()) {
                        Location location = (Location)var1.next();
                        location.getBlock().setType(Material.ANVIL);
                    }
                }

            }
        }).runTaskTimer(this.plugin, 0L, 30L);
    }

    public List<Location> getAnvilLocations() {
        return this.anvilLocations;
    }
}
