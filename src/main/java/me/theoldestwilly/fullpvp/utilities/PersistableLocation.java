package me.theoldestwilly.fullpvp.utilities;

import com.google.common.base.Preconditions;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
public class PersistableLocation implements ConfigurationSerializable, Cloneable {

    private Location location;
    private World world;
    private String worldName;

    @Setter private UUID worldUID;

    @Setter private double x, y, z;

    @Setter private float yaw, pitch;

    public PersistableLocation(Location location) {
        Preconditions.checkNotNull(location, "Location cannot be null");
        Preconditions.checkNotNull(location.getWorld(), "Locations' world cannot be null");

        world = location.getWorld();
        worldName = world.getName();

        worldUID = world.getUID();

        x = location.getX();
        y = location.getY();
        z = location.getZ();

        yaw = location.getYaw();
        pitch = location.getPitch();
    }

    public PersistableLocation(World world, double x, double y, double z) {
        worldName = world.getName();

        this.x = x;
        this.y = y;
        this.z = z;

        pitch = yaw = 0.0F;
    }

    public PersistableLocation(String worldName, double x, double y, double z) {
        this.worldName = worldName;

        this.x = x;
        this.y = y;
        this.z = z;

        pitch = yaw = 0.0F;
    }

    public PersistableLocation(Map<String, Object> args) {
        worldName = (String) args.get("worldName");

        worldUID = UUID.fromString((String) args.get("worldUID"));

        Object object = args.get("x");
        if (object instanceof String) {
            x = Double.parseDouble((String) object);
        } else {
            x = (Double) object;
        }

        object = args.get("y");
        if (object instanceof String) {
            y = Double.parseDouble((String) object);
        } else {
            y = (Double) object;
        }

        object = args.get("z");
        if (object instanceof String) {
            z = Double.parseDouble((String) object);
        } else {
            z = (Double) object;
        }

        yaw = Float.parseFloat((String) args.get("yaw"));
        pitch = Float.parseFloat((String) args.get("pitch"));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();

        result.put("worldName", worldName);

        result.put("worldUID", worldUID.toString());

        result.put("x", x);
        result.put("y", y);
        result.put("z", z);

        result.put("yaw", Float.toString(yaw));
        result.put("pitch", Float.toString(pitch));

        return result;
    }

    public World getWorld() {
        Preconditions.checkNotNull(worldUID, "World UUID cannot be null");
        Preconditions.checkNotNull(worldName, "World name cannot be null");

        return world == null ? world = Bukkit.getWorld(worldUID) : world;
    }

    public void setWorld(World world) {
        worldName = world.getName();
        worldUID = world.getUID();
        this.world = world;
    }

    public Location getLocation() {
        return location == null ? location = new Location(getWorld(), x, y, z, yaw, pitch) : location;
    }

    @Override
    public PersistableLocation clone() throws CloneNotSupportedException {
        try {
            return (PersistableLocation) super.clone();
        } catch (CloneNotSupportedException exception) {
            exception.printStackTrace();
            throw new RuntimeException();
        }
    }
}