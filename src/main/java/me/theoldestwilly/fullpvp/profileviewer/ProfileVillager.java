package me.theoldestwilly.fullpvp.profileviewer;

import lombok.Getter;
import lombok.Setter;
import me.theoldestwilly.fullpvp.Lang;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftVillager;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class ProfileVillager implements ConfigurationSerializable {
    private Villager villager;
    private Location location;
    private String customName = ChatColor.GREEN + "MY PROFILE";
    public @Setter boolean showingChallenges;
    private String name;

    /**
     *
     * @param number The name will be "Villager #" + number
     * @param location In command use it will be the executor's location
     * @param showingChallenges Under development
     */
    public ProfileVillager (String number, Location location, boolean showingChallenges) {
        this.location = location;
        this.name = !number.contains("Villager") ? "Villager #" + number : number;
        this.showingChallenges = showingChallenges;
        spawn();
    }

    public ProfileVillager (Map<String, Object> map) {
        this((String) map.get("name"), (Location) map.get("location"), (Boolean) map.get("challenges-viewer"));
    }

    public void spawn() {
        if (location != null) {
            villager = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
            villager.setAdult();
            ((CraftVillager)villager).getHandle().getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.0D);
            villager.setProfession(Villager.Profession.BLACKSMITH);
            villager.setCustomName(customName);
            villager.setCustomNameVisible(true);
        }
    }

    public void removeEntity() {
        if (villager != null) villager.remove();
    }

    public void updateViewerStatus() {
        if (showingChallenges) showingChallenges = false;
        else showingChallenges = true;
    }

    public void updateViewerStatusWithMessage(Player player) {
        if (showingChallenges) {
            showingChallenges = false;
            player.sendMessage(Lang.SUCCESS_PROFILEVILLAGER_VIEWER_DISABLED);
        } else {
            showingChallenges = true;
            player.sendMessage(Lang.SUCCESS_PROFILEVILLAGER_VIEWER_ENABLED);
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", name);
        map.put("location", location);
        map.put("challenges-viewer", showingChallenges);
        return map;
    }
}
