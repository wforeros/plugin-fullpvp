package me.theoldestwilly.fullpvp.world.serverspawn;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class ServerSpawn implements ConfigurationSerializable {
    private UUID uniqueId;
    private String name;
    private Location location;

    public ServerSpawn(UUID uniqueId, String name, Location location) {
        this.uniqueId = uniqueId != null ? uniqueId : UUID.randomUUID();
        this.name = name;
        this.location = location;
    }

    public ServerSpawn(String uniqueId, String name, Location location) {
        this(UUID.fromString(uniqueId), name, location);
    }

    public ServerSpawn (Map<String, Object> map) {
        this((String) map.get("uniqueId"), (String) map.get("name"), (Location) map.get("location"));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("uniqueId", uniqueId.toString());
        map.put("name", name);
        map.put("location", location);
        return map;
    }
}
