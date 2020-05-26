package me.theoldestwilly.fullpvp.temporal;

import lombok.Setter;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;

@SerializableAs("TemporalDroppedItem")
public class TemporalDroppedItem extends TemporalItem implements ConfigurationSerializable {
    private @Setter boolean dropped;
    public TemporalDroppedItem(int duration, ItemStack item) {
        super(Long.parseLong(String.valueOf(duration)), item);
        dropped = false;
    }

    public TemporalDroppedItem (Map<String, Object> map) {
        this((int) map.get("time"), (ItemStack) map.get("item"));
    }

    public boolean wasDropped() {
        return dropped;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("time", getDuration());
        map.put("item", getItem());
        return map;
    }
}
