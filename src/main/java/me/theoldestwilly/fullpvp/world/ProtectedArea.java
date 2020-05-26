package me.theoldestwilly.fullpvp.world;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import me.theoldestwilly.fullpvp.utilities.cuboid.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class ProtectedArea implements ConfigurationSerializable {
    protected UUID uuid;
    protected Cuboid area;

    public ProtectedArea(String uUID, String world, Integer x1, Integer y1, Integer z1, Integer x2, Integer y2, Integer z2) {
        this.area = new Cuboid(Bukkit.getWorld(world), x1, y1, z1, x2, y2, z2);
        this.uuid = uUID != null ? UUID.fromString(uUID) : UUID.randomUUID();
    }

    public ProtectedArea(Map<String, Object> map) {
        this((String)map.get("uuid"), (String)map.get("world"), (Integer)map.get("x1"), (Integer)map.get("y1"), (Integer)map.get("z1"), (Integer)map.get("x2"), (Integer)map.get("y2"), (Integer)map.get("z2"));
    }

    public ProtectedArea(Cuboid cuboid) {
        this.uuid = UUID.randomUUID();
        this.area = cuboid;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap();
        map.put("uuid", this.uuid.toString());
        map.put("x1", this.area.getX1());
        map.put("y1", this.area.getY1());
        map.put("z1", this.area.getZ1());
        map.put("x2", this.area.getX2());
        map.put("y2", this.area.getY2());
        map.put("z2", this.area.getZ2());
        map.put("world", this.area.getWorldName());
        return map;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Cuboid getArea() {
        return this.area;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setArea(Cuboid area) {
        this.area = area;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ProtectedArea)) {
            return false;
        } else {
            ProtectedArea other = (ProtectedArea)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$uuid = this.getUuid();
                Object other$uuid = other.getUuid();
                if (this$uuid == null) {
                    if (other$uuid != null) {
                        return false;
                    }
                } else if (!this$uuid.equals(other$uuid)) {
                    return false;
                }

                Object this$area = this.getArea();
                Object other$area = other.getArea();
                if (this$area == null) {
                    if (other$area != null) {
                        return false;
                    }
                } else if (!this$area.equals(other$area)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof ProtectedArea;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $uuid = this.getUuid();
        result = result * 59 + ($uuid == null ? 43 : $uuid.hashCode());
        Object $area = this.getArea();
        result = result * 59 + ($area == null ? 43 : $area.hashCode());
        return result;
    }

    public String toString() {
        return "ProtectedArea(uuid=" + this.getUuid() + ", area=" + this.getArea() + ")";
    }
}
