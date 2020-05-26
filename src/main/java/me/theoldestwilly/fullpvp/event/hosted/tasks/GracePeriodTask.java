package me.theoldestwilly.fullpvp.event.hosted.tasks;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.event.hosted.structure.HostedEvent;
import me.theoldestwilly.fullpvp.event.hosted.structure.Phase;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class GracePeriodTask extends BukkitRunnable {
    private int counter = 11;
    public boolean countdownEnded = false;
    private FullPvP plugin;
    private HostedEvent event;

    public GracePeriodTask(FullPvP plugin, HostedEvent hostedEvent) {
        this.plugin = plugin;
        this.event = hostedEvent;
        this.runTaskTimerAsynchronously(plugin, 0L, 20L);
    }

    public void run() {
        --this.counter;
        if (this.counter <= 0) {
            try {
                if (event.getAlivePlayers().size() <= 1 && Phase.GRACE_PERIOD == event.getPhase()) {
                    //participant.returnCachedInventory();
                    event.cancel("Insufficent players before grace period ends");
                    return;
                }
                this.event.setPhase(Phase.IN_GAME);
                this.event.broadcast(event.getPrefix() + ChatColor.YELLOW + "Grace period has ended, pvp is now enabled!");
                this.countdownEnded = true;
                //this.plugin.getEventsManager().setEventCooldown(this.event.getType());
                this.cancel();
            } catch (NoClassDefFoundError var2) {
                this.cancel();
                this.event.cancel("Internal error");
                System.out.println(var2);
                System.out.println("Error while removing grace period from the event!");
            }
        } else if (this.counter % 10 == 0 || this.counter <= 5) {
            this.event.broadcast(ChatColor.GREEN + "Grace period will end in " + this.counter + " seconds.");
        }

    }

    public boolean hasCountdownEnded() {
        return this.countdownEnded;
    }
}

