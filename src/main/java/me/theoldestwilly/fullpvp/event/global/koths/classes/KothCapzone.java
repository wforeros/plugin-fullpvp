package me.theoldestwilly.fullpvp.event.global.koths.classes;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import me.theoldestwilly.fullpvp.utilities.cuboid.Cuboid;
import me.theoldestwilly.fullpvp.world.ProtectedArea;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class KothCapzone implements ConfigurationSerializable {
    protected ProtectedArea protectedArea;
    protected String name;
    protected UUID caper;
    protected Long timeCaping;

    public KothCapzone(String name, ProtectedArea protectedArea) {
        this.name = name;
        this.protectedArea = protectedArea;
        this.caper = null;
        this.timeCaping = null;
    }

    public KothCapzone(Map<String, Object> map) {
        this((String)map.get("name"), (ProtectedArea)map.get("cap"));
    }

    public KothCapzone(String name, Cuboid cuboid) {
        this.protectedArea = new ProtectedArea(cuboid);
        this.name = name;
        this.caper = null;
        this.timeCaping = null;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap();
        map.put("name", this.name);
        map.put("cap", this.protectedArea);
        return map;
    }

    public ProtectedArea getProtectedArea() {
        return this.protectedArea;
    }

    public String getName() {
        return this.name;
    }

    public UUID getCaper() {
        return this.caper;
    }

    public Long getTimeCaping() {
        return this.timeCaping;
    }

    public void setProtectedArea(ProtectedArea protectedArea) {
        this.protectedArea = protectedArea;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCaper(UUID caper) {
        this.caper = caper;
    }

    public void setTimeCaping(Long timeCaping) {
        this.timeCaping = timeCaping;
    }


    protected boolean canEqual(Object other) {
        return other instanceof KothCapzone;
    }



    public String toString() {
        return "KothCapzone(protectedArea=" + this.getProtectedArea() + ", name=" + this.getName() + ", caper=" + this.getCaper() + ", timeCaping=" + this.getTimeCaping() + ")";
    }
}