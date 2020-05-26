package me.theoldestwilly.fullpvp.modes;

import lombok.Getter;
import lombok.Setter;
import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.combatlogger.event.TagApplicationEvent;
import me.theoldestwilly.fullpvp.event.global.GlobalEvent;
import me.theoldestwilly.fullpvp.event.hosted.structure.HostedEvent;
import me.theoldestwilly.fullpvp.event.scheduler.ScheduledEventStartEvent;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.GameUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Getter @Setter
public abstract class ServerMode implements Listener {
    private Long duration, startTime;
    private boolean pvpEnabled;
    private FullPvP plugin;


    public ServerMode(FullPvP plugin, boolean pvpEnabled, Long duration) {
        this.plugin = plugin;
        this.pvpEnabled = pvpEnabled;
        this.duration = duration != null ? duration : TimeUnit.MINUTES.toMillis(15);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        startTime = System.currentTimeMillis();
        GlobalEvent event = plugin.getGlobalEventsManager().getGlobalEvent();
        if (event != null) {
            plugin.getGlobalEventsManager().cancelEvent(Bukkit.getConsoleSender());
        }
    }

    public Long getRemainingTime() {
        return duration - (System.currentTimeMillis() - startTime);
    }

    public abstract List<String> getScoreboardLines();

    public void finish() {
        HandlerList.unregisterAll(this);
        getPlugin().getModesManager().setCurrentMode(null);
        GameUtils.clearChat(Bukkit.getOnlinePlayers());
        Bukkit.broadcastMessage(ChatColor.GOLD + "Global grace period finished, PvP is now " + ChatColor.RED + "ENABLED" + ChatColor.GOLD + ".");
    }

    @EventHandler
    public void onScheduledEventStart(ScheduledEventStartEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onTagApplication (TagApplicationEvent event) {
        if (!pvpEnabled) event.setCancelled(true);
    }
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!pvpEnabled) {
            HostedEvent hostedEvent = getPlugin().getHostedEventsManager().getCurrentEvent();
            Player damager = null;
            if (hostedEvent == null) {
                event.setCancelled(true);
                if (event.getDamager() instanceof Player) damager = (Player) event.getDamager();
            } else {
                if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
                    damager = (Player) event.getDamager();
                    if (!hostedEvent.isAlive(damager)) {
                        event.setCancelled(true);
                    }
                } else if (event.getDamager() instanceof Projectile) {
                    Projectile snowball = (Projectile) event.getDamager();
                    damager = (Player) snowball.getShooter();
                    if (!hostedEvent.isAlive(damager)) {
                        event.setCancelled(true);
                    }
                }
            }
            if (damager != null && event.isCancelled()) damager.sendMessage(new String[]{"", Lang.PREFIX + ChatColor.RED + "Currently PvP is not allowed in the server, use this mode to search chests, to get items and money. Watch your scoreboard to check the remaining time before pvp will be enabled.", ""});
        }
    }
}
