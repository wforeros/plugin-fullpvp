package me.theoldestwilly.fullpvp.event.global.koths.classes;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.event.Values;
import me.theoldestwilly.fullpvp.event.global.GlobalEvent;
import me.theoldestwilly.fullpvp.utilities.JavaUtils;
import me.theoldestwilly.fullpvp.utilities.cuboid.Cuboid;
import me.theoldestwilly.fullpvp.world.ProtectedArea;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

public class KothEvent extends GlobalEvent implements ConfigurationSerializable {
    private KothCapzone kothCapzone;
    private Long duration;
    private Long startTimeCaping;
    private FullPvP plugin;
    private int taskId;
    private @Getter @Setter ProtectedArea protectedArea;

    public KothEvent(String name, ProtectedArea protectedArea, KothCapzone kothCapzone, Object duration) {
        super(false);
        this.setName(name);
        this.setProtectedArea(protectedArea);
        this.kothCapzone = kothCapzone;
        this.duration = Long.valueOf(duration.toString());
        this.setActive(false);
    }

    public void startTask() {
        this.taskId = Bukkit.getScheduler().scheduleAsyncRepeatingTask(this.plugin, new Runnable() {
            public void run() {
                KothEvent.this.plugin.getGlobalEventsManager().getKothManager().getKothInteractManager().onPlayerCaptureKoth(KothEvent.this);
            }
        }, 0L, 300L);
    }

    public void cancelTask() {
        Bukkit.getScheduler().cancelTask(this.taskId);
    }

    public KothEvent(Map<String, Object> map) {
        this((String)map.get("name"), (ProtectedArea)map.get("area"), (KothCapzone)map.get("capzone"), map.get("duration"));
    }

    public KothEvent(String name, ProtectedArea protectedArea) {
        super(false);
        this.setName(name);
        this.setProtectedArea(protectedArea);
        this.kothCapzone = null;
        this.setActive(false);
    }

    public void setKothCapzone(String name, Cuboid cuboid) {
        this.kothCapzone = new KothCapzone(name, cuboid);
    }

    public Long getRemaining() {
        return this.getStartTimeCaping() != null ? this.duration - (System.currentTimeMillis() - this.getStartTimeCaping()) : this.duration;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap();
        map.put("name", this.getName());
        map.put("duration", this.duration);
        map.put("area", this.getProtectedArea());
        map.put("capzone", this.kothCapzone);
        return map;
    }
    /**
     * This method does not do anything haha use the koth manager
     *
     */
    public void startEvent() {
    }

    /**
     * This method does not do anything haha use the koth manager
     *
     */
    public void finishEvent(Player winner) {
    }

    public String getDisplayName() {
        return Values.KOTH_DISPLAY_NAME;
    }

    public List getScoreboardLines() {
        Cuboid cuboid = this.getProtectedArea().getArea();
        List<String> lines = Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&6Koth "), ChatColor.translateAlternateColorCodes('&', " &7» &e" + this.getName() + " &7(" + cuboid.getCenter().getBlockX() + " | " + cuboid.getCenter().getBlockZ() + ")"), ChatColor.translateAlternateColorCodes('&', " &7» &eRemaining&7: &f" + JavaUtils.setFormat(this.getRemaining())));
        return lines;
    }

    /**
     * This method does not do anything haha use the koth manager
     * @param plugin
     */
    public void cancelEvent(FullPvP plugin) {

    }

    public KothCapzone getKothCapzone() {
        return this.kothCapzone;
    }

    public Long getDuration() {
        return this.duration;
    }

    public Long getStartTimeCaping() {
        return this.startTimeCaping;
    }

    public FullPvP getPlugin() {
        return this.plugin;
    }

    public int getTaskId() {
        return this.taskId;
    }

    public void setKothCapzone(KothCapzone kothCapzone) {
        this.kothCapzone = kothCapzone;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public void setStartTimeCaping(Long startTimeCaping) {
        this.startTimeCaping = startTimeCaping;
    }

    public void setPlugin(FullPvP plugin) {
        this.plugin = plugin;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof KothEvent)) {
            return false;
        } else {
            KothEvent other = (KothEvent)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label63: {
                    Object this$kothCapzone = this.getKothCapzone();
                    Object other$kothCapzone = other.getKothCapzone();
                    if (this$kothCapzone == null) {
                        if (other$kothCapzone == null) {
                            break label63;
                        }
                    } else if (this$kothCapzone.equals(other$kothCapzone)) {
                        break label63;
                    }

                    return false;
                }

                Object this$duration = this.getDuration();
                Object other$duration = other.getDuration();
                if (this$duration == null) {
                    if (other$duration != null) {
                        return false;
                    }
                } else if (!this$duration.equals(other$duration)) {
                    return false;
                }

                Object this$startTimeCaping = this.getStartTimeCaping();
                Object other$startTimeCaping = other.getStartTimeCaping();
                if (this$startTimeCaping == null) {
                    if (other$startTimeCaping != null) {
                        return false;
                    }
                } else if (!this$startTimeCaping.equals(other$startTimeCaping)) {
                    return false;
                }

                label42: {
                    Object this$plugin = this.getPlugin();
                    Object other$plugin = other.getPlugin();
                    if (this$plugin == null) {
                        if (other$plugin == null) {
                            break label42;
                        }
                    } else if (this$plugin.equals(other$plugin)) {
                        break label42;
                    }

                    return false;
                }

                if (this.getTaskId() != other.getTaskId()) {
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof KothEvent;
    }


    public String toString() {
        return "KothEvent(kothCapzone=" + this.getKothCapzone() + ", duration=" + this.getDuration() + ", startTimeCaping=" + this.getStartTimeCaping() + ", plugin=" + this.getPlugin() + ", taskId=" + this.getTaskId() + ")";
    }
}
