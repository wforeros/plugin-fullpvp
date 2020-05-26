package me.theoldestwilly.fullpvp.utilities.cuboid;


import org.bukkit.Location;
import org.bukkit.World;

import java.util.Map;

public class NamedCuboid extends Cuboid {
    protected String name;

    public NamedCuboid(Cuboid other) {
        super(other.getWorld(), other.x1, other.y1, other.z1, other.x2, other.y2, other.z2);
    }

    public NamedCuboid(World world, int x1, int y1, int z1, int x2, int y2, int z2) {
        super(world, x1, y1, z1, x2, y2, z2);
    }

    public NamedCuboid(Location location) {
        super(location, location);
    }

    public NamedCuboid(Location first, Location second) {
        super(first, second);
    }

    public NamedCuboid(Map<String, Object> map) {
        super(map);
        this.name = (String)map.get("name");
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put("name", this.name);
        return map;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NamedCuboid clone() {
        return (NamedCuboid)super.clone();
    }

    public String toString() {
        return "NamedCuboid: " + this.worldName + ',' + this.x1 + ',' + this.y1 + ',' + this.z1 + "=>" + this.x2 + ',' + this.y2 + ',' + this.z2 + ':' + this.name;
    }
}
