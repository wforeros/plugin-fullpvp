package me.theoldestwilly.fullpvp.event.scheduler;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.event.GlobalEventType;
import me.theoldestwilly.fullpvp.utilities.JavaUtils;
import org.bukkit.Bukkit;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

@Getter
public class ScheduledEvent {
    private List<DayOfWeek> days = new ArrayList<>();
    private LocalTime scheduledTime;
    private GlobalEventType globalEventType;
    private String eventName;
    private @Setter boolean active;

    /**
     *
     * @param s It contains all data about time and which event is going to be started
     *          Order day:hour:minute:eventtype:eventname
     *          If you need 2 or more days separate it using ","
     *          Example monday,tuesday:20:0:koth:cracks
     */
    public ScheduledEvent(String s) {
        String[] split = s.split(":");
        if (split.length >= 4) {
            for (String day  : split[0].split(",")) {
                days.add(dayFromString(day));
            }
            int hour, minute;
            hour = JavaUtils.tryParseInt(split[1]);
            minute = JavaUtils.tryParseInt(split[2]);
            globalEventType = GlobalEventType.fromString(split[3]);
            eventName = split[4];
            Preconditions.checkNotNull(globalEventType);
            Preconditions.checkNotNull(eventName);
            scheduledTime = LocalTime.of(hour, minute);
        } else Bukkit.getLogger().log(Level.WARNING,  "Error while loading scheduled FullPvP event, string array length " + split.length);
    }

    private DayOfWeek dayFromString(String s) {
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) if (dayOfWeek.toString().equalsIgnoreCase(s)) return dayOfWeek;
        return null;
    }

    public boolean isTimeToStart(LocalDateTime time) {
        return (!active && time.getHour() == scheduledTime.getHour() && time.getMinute() == scheduledTime.getMinute() && days.contains(time.getDayOfWeek()));
    }

    public void startEvent() {
        ScheduledEventStartEvent event = new ScheduledEventStartEvent(this);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            if (globalEventType == GlobalEventType.KOTH) {
                FullPvP.getInstance().getGlobalEventsManager().onStartNewEvent(Bukkit.getConsoleSender(), eventName, GlobalEventType.KOTH, this);
                setActive(true);
            } else if (globalEventType == GlobalEventType.DROPPER) {
                FullPvP.getInstance().getGlobalEventsManager().onStartNewEvent(Bukkit.getConsoleSender(), "", GlobalEventType.DROPPER, this);
                setActive(true);
            } else if (globalEventType == GlobalEventType.DTC) {
                FullPvP.getInstance().getGlobalEventsManager().onStartNewEvent(Bukkit.getConsoleSender(), eventName, GlobalEventType.DTC, this);
                setActive(true);
            }
        }
    }
}
