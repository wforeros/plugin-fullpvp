package me.theoldestwilly.fullpvp.event.hosted.tasks;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.event.hosted.structure.HostedEvent;
import me.theoldestwilly.fullpvp.utilities.chat.ClickAction;
import me.theoldestwilly.fullpvp.utilities.chat.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;

public class CountdownTask extends BukkitRunnable {
    private int counter;
    public boolean countdownEnded = false;
    private FullPvP plugin;
    private HostedEvent event;

    public CountdownTask(FullPvP plugin, int seconds, HostedEvent hostedEvent) {
        this.plugin = plugin;
        this.counter = seconds;
        this.event = hostedEvent;
        this.runTaskTimerAsynchronously(plugin, 20L, 20L);
    }

    public void run() {
        if ((this.counter % 15 == 0 || this.counter <= 3) && this.counter >= 1) {
            Text text = new Text(this.event.getHostWithRank());
            text.append((new Text(" is hosting the ")).setColor(ChatColor.WHITE));
            text.append(new Text(this.event.getDisplayName()));
            text.append(new Text(" event "));
            text.append(new Text("starting in " + this.counter + " seconds."));
            text.append((new Text(" [Click here to join]")).setColor(ChatColor.GRAY).setItalic(true).setClick(ClickAction.RUN_COMMAND, "/evg join").setHoverText(ChatColor.GREEN + "Click here to join the event"));
            Iterator iterator = Bukkit.getOnlinePlayers().iterator();
            while(iterator.hasNext()) {
                Player player = (Player)iterator.next();
                text.send(player);
            }
        } else if (this.counter <= 0) {
            if (this.event.getEventPlayers().size() < this.event.getRequiredPlayers()) {
                Bukkit.getScheduler().runTask(plugin, () -> this.event.cancel("Insufficent players"));
            } else if (this.event != null) {
                try {
                    this.event.preStart();
                } catch (NoClassDefFoundError var4) {
                    System.out.println(var4);
                    System.out.println("Error while starting the event!");
                }
                this.countdownEnded = true;
            }
            this.cancel();
        }
        --this.counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public boolean hasCountdownEnded() {
        return this.countdownEnded;
    }
}
