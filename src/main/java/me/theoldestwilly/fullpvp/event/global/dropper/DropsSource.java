package me.theoldestwilly.fullpvp.event.global.dropper;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class DropsSource implements ConfigurationSerializable {

    /**
     * Used only to check if this source was already used
     */
    private @Setter int initialUses;
    private @Setter int currentUses;
    private Location location;

    public DropsSource(Location location, Integer uses, boolean setIronBlock) {
        this.location = location;
        this.initialUses = uses != null ? uses : 1;
        if (setIronBlock) location.getBlock().setType(Material.IRON_BLOCK);
        currentUses = initialUses;
    }

    public DropsSource (Map<String , Object> map) {
        this ((Location) map.get("location"), (int) map.get("max-uses"), false);
    }

    public void spawnItem(ItemStack item) {
        location.getWorld().dropItem(location.getBlock().getLocation().add(0.5, 2, 0.5), item).setVelocity(new Vector(0, 0, 0));
        currentUses--;
    }

    public boolean isAvailable() {
        return currentUses > 0;
    }

    public void resetUses() {
        currentUses = initialUses;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("max-uses", initialUses);
        map.put("location", location);
        return map;
    }
}
