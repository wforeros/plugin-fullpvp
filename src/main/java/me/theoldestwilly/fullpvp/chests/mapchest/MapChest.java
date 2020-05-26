package me.theoldestwilly.fullpvp.chests.mapchest;

import lombok.Getter;
import lombok.Setter;
import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.users.Profile;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.GameUtils;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.PluginError;
import org.bukkit.*;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class MapChest implements ConfigurationSerializable {
    private @Getter
    @Setter
    ChestItem[] contents;
    private @Getter
    @Setter
    Location location;
    private @Getter
    @Setter
    Long delay;
    private @Getter
    @Setter
    String name;
    private @Getter
    UUID uniqueID;
    private @Getter
    double coordX;
    private @Getter
    double coordY;
    private @Getter
    double coordZ;
    private ChestItem[] serializedItems;

    public MapChest(String name, Inventory inventory, Location location, Long delay) {
        this.name = name;
        setContents(inventory.getContents());
        this.location = location;
        this.delay = delay;
        this.uniqueID = UUID.randomUUID();
        this.coordX = location.getX();
        this.coordY = location.getY();
        this.coordZ = location.getZ();
    }

    public MapChest(Map<String, Object> map) {
        this((String)map.get("uuid"), map.get("contents"), map.get("delay"), (String)map.get("name"), (Location) map.get("location"));
    }

    public MapChest(String uuid, Object contents, Object delay, String name, Location location) {
        this.uniqueID = uuid != null ? UUID.fromString(uuid):UUID.randomUUID();
        this.name = name;
        this.delay = new Long(delay.toString());
        this.setContents(contents);
        this.location = location;
        this.coordX = location.getX();
        this.coordY = location.getY();
        this.coordZ = location.getZ();
    }

    public void setContents(Object contents) {
        this.contents = null;
        if (contents instanceof List) {
            ChestItem[] newContents = (ChestItem[]) ((List) contents).toArray(new ChestItem[0]);
            this.contents = newContents;
        } else if (contents instanceof ChestItem[]) {
            this.contents = (ChestItem[]) contents;
        } else if (contents instanceof ItemStack[]) {
            this.contents = GameUtils.convertContentsToChestItemsArray((ItemStack[]) contents);
        }
    }

    public Inventory createInventory() {
        return Bukkit.createInventory(null, 9 * 3,getDisplayName());
    }

    public void openChest(Player player, Profile profile, boolean isDelayed) {
        profile = profile != null ? profile : FullPvP.getInstance().getProfileHandler().getProfileByUniqueID(player.getUniqueId());
        if (profile == null) {
            player.sendMessage(Lang.ERROR_COMMON_MESSAGE(PluginError.ERROR_LOADING_PROFILE) + ChatColor.GRAY + " (Open Chest)");
            return;
        }
        Inventory inventory = createInventory();
        if (!isDelayed) {
            for (ChestItem chestItem : this.contents)
                inventory.setItem(chestItem.getSlot(), chestItem.getItemStack());
            profile.addDelayedMapChest(uniqueID, inventory);
        } else {
            inventory = profile.getNoDelayedMapChestInventory(getUniqueID());
        }
        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.CHEST_OPEN, 1f, 1f);
    }

    public String getDisplayName() {
        return ChatColor.GRAY + getName();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap();
        map.put("uuid", String.valueOf(this.uniqueID));
        map.put("name", this.name);
        map.put("contents", this.contents);
        map.put("location", this.location);
        map.put("delay", this.delay);
        return map;
    }

    public void executeItemsSerialization() {
        /*if (inventory != null) {
            List<ChestItem> list = new ArrayList<>();
            int i = 0;
            for (ItemStack itemStack : inventory.getContents()) {
                if (itemStack != null && itemStack.getType() != Material.AIR)
                    list.add(new ChestItem(i, itemStack));
                i++;
            }
            serializedItems = list.toArray(new ChestItem[0]);
        }*/
    }
}
