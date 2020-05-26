package me.theoldestwilly.fullpvp.event.global;


import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.event.scheduler.Schedulable;
import me.theoldestwilly.fullpvp.world.ProtectedArea;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public abstract class GlobalEvent implements Schedulable {
    private String name;
    private @Getter FullPvP plugin;
    private boolean active;
    private boolean scheduled;
    private List<ItemStack> rewards = new ArrayList();

    public GlobalEvent(boolean scheduled) {
        this.scheduled = scheduled;
        this.plugin = FullPvP.getInstance();
    }

    public GlobalEvent (FullPvP plugin, boolean scheduled) {
        this.plugin = plugin;
    }

    public abstract void startEvent();

    public abstract void finishEvent(Player var2);

    public abstract String getDisplayName();

    public String getPrefix() {
        return ChatColor.GRAY + "[" + getDisplayName() + ChatColor.GRAY + "] ";
    }

    public abstract List<String> getScoreboardLines();

    public abstract void cancelEvent(FullPvP var1);

    public void giveRewards(Player winner) {
        winner.getInventory().addItem((ItemStack[])this.getRewards().toArray(new ItemStack[0]));
    }

    /*public ProtectedArea getProtectedArea() {
        return this.protectedArea;
    }*/

    public String getName() {
        return this.name;
    }

    public boolean isActive() {
        return this.active;
    }

    public List<ItemStack> getRewards() {
        return this.rewards;
    }

    /*public void setProtectedArea(ProtectedArea protectedArea) {
        this.protectedArea = protectedArea;
    }*/

    public void setName(String name) {
        this.name = name;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setRewards(List<ItemStack> rewards) {
        this.rewards = rewards;
    }

    @Override
    public boolean wasScheduled() {
        return scheduled;
    }

    @Override
    public void setScheduled(boolean b) {
        scheduled = b;
    }
}
