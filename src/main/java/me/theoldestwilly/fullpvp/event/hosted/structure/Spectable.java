package me.theoldestwilly.fullpvp.event.hosted.structure;

import org.bukkit.Location;

public interface Spectable {
    void setSpectatorsSpawn(Location location);
    boolean isSpectable();
}
