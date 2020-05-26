package me.theoldestwilly.fullpvp.temporal;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public abstract class TemporalItem {
    public Long duration;
    public Long startedTime;
    public ItemStack item;

    public TemporalItem(Long duration, ItemStack item) {
        this.duration = duration;
        this.startedTime = startedTime;
        this.item = item;
    }

    public void startSale() {
        startedTime = System.currentTimeMillis();
    }

    public Long getRemaining() {
        return duration - (System.currentTimeMillis() - startedTime);
    }
}
