package me.theoldestwilly.fullpvp.chests.mapchest;

import lombok.Getter;
import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.utilities.Config;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.ConvertFormat;
import net.silexpvp.nightmare.util.GenericUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class MapChestManager {
    private FullPvP plugin;
    private int reference = 1;
    private String nameNode = "Chest #";
    private String alias = "mapchest";
    private @Getter Config config;
    private @Getter List<MapChest> mapChests = new ArrayList<>();
    public MapChestManager(FullPvP plugin) {
        this.plugin = plugin;
        ConfigurationSerialization.registerClass(ChestItem.class, "ChestItem");
        ConfigurationSerialization.registerClass(MapChest.class, "MapChest");
        config = new Config(plugin, "mapchest");
        loadMapChests();
    }

    public void loadMapChests() {
        Object object = this.config.get(alias);
        if (object != null && object instanceof List) {
            this.mapChests.addAll(GenericUtils.createList(object, MapChest.class));
            reloadMapChests(Bukkit.getConsoleSender());
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + ChatColor.YELLOW + reference + " MapChests successfully loaded.");
        }
    }

    public void saveMapChests() {
        for (MapChest mapChest : mapChests) {
            mapChest.executeItemsSerialization();
        }
        this.config.set(alias, this.mapChests);
        this.config.save();
    }

    public void reloadMapChests(CommandSender sender) {
        MapChest mapChest;
        for (int i = 0; i < mapChests.size(); i++) {
            mapChest = mapChests.get(i);
            if (mapChest.getLocation().getBlock().getType() == Material.CHEST) {
                mapChest.setName(nameNode + (i + 1));
                //mapChest.reloadInventory();
            } else {
                mapChests.remove(mapChest);
            }
        }
        reference = mapChests.size();
        Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Map chests reloaded.", false);
    }

    public void saveMapChest(Player player, String[] arguments) {
        HashSet<Byte> set = null;
        Block block = player.getTargetBlock(set, 25);
        if (block.getType() == Material.CHEST) {
            Chest chest = (Chest) block.getState();
            Inventory inventory = chest.getBlockInventory();
            if (inventory != null && inventory.getSize() != 0) {
                MapChest mapChest = getMapChestByLocation(chest.getLocation());
                if (mapChest == null) {
                    List<String> delayList = new ArrayList<>();
                    for (int i = 1; i < arguments.length; i++) {
                        delayList.add(arguments[i]);
                    }
                    Long delay = ConvertFormat.convertDelay2Long2(delayList.toArray(new String[0]), player);
                    if (delay != null) {
                        mapChests.add((mapChest = new MapChest(getNewName(), inventory, chest.getLocation(), delay)));
                        reference++;
                        player.sendMessage(Lang.SUCCESS_MAPCHEST_CREATED(mapChest));
                    } else player.sendMessage(Lang.ERROR_ECONOMY_INVALID_VALUE);
                } else {
                    player.sendMessage(Lang.ERROR_DB_ALREADY_CONTAINS_OBJECT);
                }
            } else {
                player.sendMessage(Lang.ERROR_CHEST_IS_EMPTY);
            }
        } else {
            player.sendMessage(Lang.ERROR_NOT_LOOKING_REQUIRED_OBJECT("chest"));
        }
    }

    public void removeMapChest(Player player) {
        HashSet<Byte> set = null;
        Block block = player.getTargetBlock(set, 25);
        if (block.getType() == Material.CHEST) {
            MapChest mapChest = getMapChestByLocation(block.getLocation());
            if (mapChest != null) {
                mapChests.remove(mapChest);
                player.sendMessage(Lang.SUCCESS_OBJECT_REMOVED_FROM_DB);
            } else {
                player.sendMessage(Lang.ERROR_DB_DOESNT_CONTAINS_OBJECT);
            }
        } else {
            player.sendMessage(Lang.ERROR_NOT_LOOKING_REQUIRED_OBJECT("chest"));
        }
    }

    public MapChest getMapChestByLocation(Location location) {
        for (MapChest mapChest : mapChests) {
            if (mapChest.getCoordX() == location.getX() && location.getY() == mapChest.getCoordY()
                    && location.getZ() == mapChest.getCoordZ())
                return mapChest;
        }
        return null;
    }

    public MapChest getMapChestByName(String name) {
        String originalName = ChatColor.stripColor(name);
        return this.mapChests.stream().filter((mapChest) -> {
                    return mapChest.getName().equalsIgnoreCase(originalName);
                }
        ).findFirst().orElse(null);
    }

    public MapChest getMapChestByUniqueId(UUID uniqueId) {
        return this.mapChests.stream().filter((mapChest) ->
                mapChest.getUniqueID().equals(uniqueId)
        ).findFirst().orElse(null);
    }



    private String getNewName() {
        return nameNode + (reference + 1);
    }

}
