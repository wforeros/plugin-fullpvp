package me.theoldestwilly.fullpvp.kits;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.chests.mapchest.MapChest;
import me.theoldestwilly.fullpvp.utilities.Config;
import net.silexpvp.nightmare.util.GenericUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

public class KitManager {
    private final FullPvP plugin;
    private @Getter Config config;
    private String alias = "kits";
    private final List<Kit> kits = new ArrayList();

    public KitManager(FullPvP plugin) {
        this.plugin = plugin;
        ConfigurationSerialization.registerClass(Kit.class);
        config = new Config(plugin, alias);
        this.loadKits();
    }

    public void loadKits() {
        Object object = this.config.get(alias);
        if (object != null && object instanceof List) {
            this.kits.addAll(GenericUtils.createList(object, Kit.class));
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + ChatColor.YELLOW + kits.size() + " Kits successfully loaded.");
        }
    }

    public void saveKits() {
        this.config.set(alias, this.kits);
        this.config.save();
    }

    public Kit getKit(String name) {
        return (Kit)this.kits.stream().filter((kit) -> {
            return kit.getName().equalsIgnoreCase(name);
        }).findFirst().orElse(null);
    }

    public Kit getKit(UUID uniqueId) {
        return (Kit)this.kits.stream().filter((kit) -> {
            return kit.getUniqueId().equals(uniqueId);
        }).findFirst().orElse(null);
    }

    public void createKit(Kit kit) {
        this.kits.add(kit);
    }

    public void removeKit(Kit kit) {
        this.kits.remove(kit);
    }

    public List<Kit> getKits() {
        return this.kits;
    }
}
