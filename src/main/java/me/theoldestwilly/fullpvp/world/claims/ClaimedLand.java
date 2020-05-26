package me.theoldestwilly.fullpvp.world.claims;

import java.util.LinkedHashMap;
import java.util.Map;

import me.theoldestwilly.fullpvp.utilities.cuboid.Cuboid;
import me.theoldestwilly.fullpvp.world.ProtectedArea;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class ClaimedLand implements ConfigurationSerializable {
    protected String name;
    protected ProtectedArea protectedArea;
    protected String permission;
    protected boolean broadcastAllowed;
    protected boolean FlyAllowed;
    protected boolean pvpAllowed;
    protected boolean pearlAllowed;

    public ClaimedLand(String name, ProtectedArea protectedArea, Boolean broadcastAllowed, Boolean flyAllowed, Boolean pvpAllowed, Boolean pearlAllowed) {
        this.name = name;
        this.protectedArea = protectedArea;
        this.broadcastAllowed = broadcastAllowed;
        this.FlyAllowed = flyAllowed;
        this.permission = "fullpvp.claims.join." + name.toLowerCase();
        this.pvpAllowed = pvpAllowed;
        this.pearlAllowed = pearlAllowed;
    }

    public ClaimedLand(Map<String, Object> map) {
        this((String)map.get("name"), (ProtectedArea)map.get("area"), (Boolean)map.get("broadcast"), (Boolean)map.get("fly"), (Boolean)map.get("pvp"), (Boolean)map.get("pearl"));
    }

    public ClaimedLand(String name, Cuboid cuboid) {
        ProtectedArea protectedArea = new ProtectedArea(cuboid);
        this.name = name;
        this.protectedArea = protectedArea;
        this.broadcastAllowed = true;
        this.FlyAllowed = false;
        this.pvpAllowed = false;
        this.pearlAllowed = false;
        this.permission = "fullpvp.claims.join." + name.toLowerCase();
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap();
        map.put("name", this.name);
        map.put("area", this.protectedArea);
        map.put("broadcast", this.broadcastAllowed);
        map.put("fly", this.FlyAllowed);
        map.put("pvp", this.pvpAllowed);
        map.put("pearl", this.pearlAllowed);
        return map;
    }

    public String getName() {
        return this.name;
    }

    public ProtectedArea getProtectedArea() {
        return this.protectedArea;
    }

    public String getPermission() {
        return this.permission;
    }

    public boolean isBroadcastAllowed() {
        return this.broadcastAllowed;
    }

    public boolean isFlyAllowed() {
        return this.FlyAllowed;
    }

    public boolean isPvpAllowed() {
        return this.pvpAllowed;
    }

    public boolean isPearlAllowed() {
        return this.pearlAllowed;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProtectedArea(ProtectedArea protectedArea) {
        this.protectedArea = protectedArea;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void setBroadcastAllowed(boolean broadcastAllowed) {
        this.broadcastAllowed = broadcastAllowed;
    }

    public void setFlyAllowed(boolean FlyAllowed) {
        this.FlyAllowed = FlyAllowed;
    }

    public void setPvpAllowed(boolean pvpAllowed) {
        this.pvpAllowed = pvpAllowed;
    }

    public void setPearlAllowed(boolean pearlAllowed) {
        this.pearlAllowed = pearlAllowed;
    }
}
