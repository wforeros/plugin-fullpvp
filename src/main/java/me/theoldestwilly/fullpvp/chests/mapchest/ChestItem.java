package me.theoldestwilly.fullpvp.chests.mapchest;

import lombok.Getter;
import lombok.Setter;
import me.theoldestwilly.fullpvp.utilities.ItemStackSerializer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;

public class ChestItem implements ConfigurationSerializable {
    private @Getter
    @Setter
    int slot;
    private @Getter @Setter
    ItemStack itemStack;

    public ChestItem(int slot, ItemStack itemStack) {
        this.slot = slot;
        this.itemStack = itemStack;
    }

    public ChestItem(Map<String, Object> map) {
        this((int)map.get("slot"), (ItemStack) map.get("item-info"));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap();
        map.put("slot", this.slot);
        map.put("item-info", itemStack);
        return map;
    }

    @Override
    public String toString() {
        return getSlot() + ", " + ItemStackSerializer.serialize(getItemStack());
    }
}