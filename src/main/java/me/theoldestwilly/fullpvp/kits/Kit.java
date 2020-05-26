package me.theoldestwilly.fullpvp.kits;


import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.kits.event.PlayerKitApplyEvent;
import me.theoldestwilly.fullpvp.users.Profile;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SerializableAs("Kit")
public class Kit implements ConfigurationSerializable {
    public static String KIT_PERMISSION_NODE = "fullpvp.kit.%1$s";
    private final UUID uniqueId;
    private String name;
    private String description;
    private boolean disabled = true;
    private boolean wipeInventory = false;
    private ItemStack icon;
    private ItemStack[] contents;
    private ItemStack[] armour;
    private long cooldown = 0L;
    private Integer maximumUses = 2147483647;

    public Kit(String name, String description, Inventory inventory) {
        this.uniqueId = UUID.randomUUID();
        this.name = name;
        this.description = description;
        if (inventory.getType() == InventoryType.PLAYER) {
            PlayerInventory playerInventory = (PlayerInventory)inventory;
            this.armour = playerInventory.getArmorContents();
            this.setIcon(playerInventory.getItemInHand());
        }

        this.contents = inventory.getContents();
    }

    public Kit(Map<String, Object> map) {
        this.uniqueId = UUID.fromString((String)map.get("uniqueId"));
        Object serialized = map.get("name");
        if (serialized != null) {
            this.name = (String)map.get("name");
        }

        serialized = map.get("description");
        if (serialized != null) {
            this.description = (String)map.get("description");
        }

        serialized = map.get("disabled");
        if (serialized != null) {
            this.disabled = (Boolean)map.get("disabled");
        }

        serialized = map.get("wipeInventory");
        if (serialized != null) {
            this.wipeInventory = (Boolean)map.get("wipeInventory");
        }

        serialized = map.get("icon");
        if (serialized != null) {
            this.icon = (ItemStack)map.get("icon");
        }

        serialized = map.get("contents");
        List armour;
        if (serialized != null && serialized instanceof List) {
            armour = (List)serialized;
            this.contents = (ItemStack[])((ItemStack[])armour.toArray(new ItemStack[armour.size()]));
        }

        serialized = map.get("armour");
        if (serialized != null && serialized instanceof List) {
            armour = (List)serialized;
            this.armour = (ItemStack[])((ItemStack[])armour.toArray(new ItemStack[armour.size()]));
        }

        serialized = map.get("cooldown");
        if (serialized != null) {
            this.cooldown = Long.parseLong((String)map.get("cooldown"));
        }

        serialized = map.get("maximumUses");
        if (serialized != null) {
            this.maximumUses = (Integer)map.get("maximumUses");
        }

    }

    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap();
        result.put("uniqueId", this.uniqueId.toString());
        result.put("name", this.name);
        result.put("description", this.description);
        result.put("disabled", this.disabled);
        result.put("wipeInventory", this.wipeInventory);
        result.put("icon", this.icon);
        result.put("contents", this.contents);
        result.put("armour", this.armour);
        result.put("cooldown", Long.toString(this.cooldown));
        result.put("maximumUses", this.maximumUses);
        return result;
    }

    public ItemStack getIcon() {
        return this.icon != null && this.icon.getType() != Material.AIR ? this.icon.clone() : new ItemStack(Material.CHEST);
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon != null && icon.getType() != Material.AIR ? icon.clone() : null;
    }

    public boolean apply(Player player, boolean forced) {
        PlayerKitApplyEvent calledEvent = new PlayerKitApplyEvent(player, this, forced);
        Bukkit.getPluginManager().callEvent(calledEvent);
        if (calledEvent.isCancelled()) {
            return false;
        } else {
            PlayerInventory inventory = player.getInventory();
            if (this.wipeInventory) {
                inventory.setContents(this.contents);
                inventory.setArmorContents(this.armour);
                player.sendMessage(ChatColor.GREEN + "Inventory updated with Kit '" + this.name + "'.");
            } else {
                Profile profile = FullPvP.getInstance().getProfileHandler().getProfileByUniqueID(player.getUniqueId());
                if (profile.hasKitDelayed(this) && !player.isOp()) {
                    player.sendMessage(ChatColor.RED + "Remaining delay to use this kit: " + ChatColor.GRAY + DurationFormatUtils.formatDurationWords(profile.getRemainingKitCooldown(this), true, true) + ChatColor.RED + ".");
                    return false;
                }
                ItemStack[] var5 = this.contents;
                int var6 = var5.length;
                int slot;
                ItemStack previous;
                for(slot = 0; slot < var6; ++slot) {
                    previous = var5[slot];
                    if (previous != null && previous.getType() != Material.AIR) {
                        if (inventory.firstEmpty() == -1) {
                            player.getLocation().getWorld().dropItemNaturally(player.getLocation(), previous);
                        } else {
                            inventory.addItem(new ItemStack[]{previous});
                        }
                    }
                }

                for(int i = Math.min(3, this.armour.length); i >= 0; --i) {
                    ItemStack item = this.armour[i];
                    if (item != null && item.getType() != Material.AIR) {
                        slot = i + 36;
                        previous = inventory.getItem(slot);
                        if (previous != null && previous.getType() != Material.AIR) {
                            player.getLocation().getWorld().dropItemNaturally(player.getLocation(), item);
                        } else {
                            inventory.setItem(slot, item);
                        }
                    }
                }
                if (!player.isOp()) profile.addKitCooldown(this);
                player.sendMessage(ChatColor.GREEN + "Kit '" + this.name + "' has been applied.");
            }

            player.updateInventory();
            return true;
        }
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    public boolean isWipeInventory() {
        return this.wipeInventory;
    }

    public ItemStack[] getContents() {
        return this.contents;
    }

    public ItemStack[] getArmour() {
        return this.armour;
    }

    public long getCooldown() {
        return this.cooldown;
    }

    public Integer getMaximumUses() {
        return this.maximumUses;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void setWipeInventory(boolean wipeInventory) {
        this.wipeInventory = wipeInventory;
    }

    public void setContents(ItemStack[] contents) {
        this.contents = contents;
    }

    public void setArmour(ItemStack[] armour) {
        this.armour = armour;
    }

    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
    }

    public void setMaximumUses(Integer maximumUses) {
        this.maximumUses = maximumUses;
    }
}
