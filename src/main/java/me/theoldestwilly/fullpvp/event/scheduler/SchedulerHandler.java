package me.theoldestwilly.fullpvp.event.scheduler;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SchedulerHandler implements Listener {
    private FullPvP plugin;
    private List<ScheduledEvent> scheduledEvents = new ArrayList<>();
    public SchedulerHandler(FullPvP plugin) {
        this.plugin = plugin;
        loadSchedulers();
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, eventsTask(), 60L, 20L);
    }

    public void loadSchedulers() {
        Object object = plugin.getConfig().get("scheduled-events");
        if (object instanceof List) {
            for (Object o : (List) object) {
                scheduledEvents.add(new ScheduledEvent(o.toString()));
            }
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + Lang.SUCCESS_COLOR + scheduledEvents.size() + " scheduled events loaded.");
        }
    }

    public ScheduledEvent getScheduledEvent(String name) {
        return scheduledEvents.stream().filter(event -> event.getEventName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public BukkitRunnable eventsTask() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (!scheduledEvents.isEmpty()) {
                    ScheduledEvent event = scheduledEvents.stream().filter(scheduledEvent -> scheduledEvent.isTimeToStart(LocalDateTime.now())).findFirst().orElse(null);
                    if (event != null) event.startEvent();
                }
            }
        };
    }

    @EventHandler
    public void onScheduledEventFinish (ScheduledEventFinishEvent event) {
        ScheduledEvent scheduledEvent = getScheduledEvent(event.getScheduledlEventName());
        if (scheduledEvent != null) {
            scheduledEvent.setActive(false);
        }
    }
}
