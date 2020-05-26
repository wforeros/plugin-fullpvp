package me.theoldestwilly.fullpvp.event.hosted.events;

import lombok.Getter;
import me.theoldestwilly.fullpvp.event.hosted.structure.HostedEventType;
import me.theoldestwilly.fullpvp.event.hosted.structure.HostedIndividualEvent;
import me.theoldestwilly.fullpvp.event.hosted.structure.Phase;
import me.theoldestwilly.fullpvp.event.hosted.structure.Spectable;
import me.theoldestwilly.fullpvp.users.Profile;
import me.theoldestwilly.fullpvp.utilities.JavaUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class LastManStandingEvent extends HostedIndividualEvent implements Spectable {

    private @Getter Location spawn;

    public LastManStandingEvent(Player hoster) {
        super(hoster, HostedEventType.LMS, true);
        spawn = (Location) getPlugin().getHostedEventsManager().getConfig().get(getType().toString().toLowerCase() + ".spawn");
        if (spawn == null) {
            cancel(ChatColor.RED + "Arena not found.");
            setPossibleToStart(false);
        }
        if (getSpectatorsSpawn() == null) setSpectatorsSpawn(spawn);
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void start() {
        getAlivePlayers().stream().forEach(player -> player.teleport(spawn));
    }

    @Override
    public List<String> getScoreboardLines() {
        List<String> s = new LinkedList<>();
        s.addAll(Arrays.asList(ChatColor.GOLD + "Event " + getDisplayName(), ChatColor.GOLD + "Host" + ChatColor.GRAY + ": " + ChatColor.GREEN + getHostName()));
        s.add(ChatColor.GOLD + "Participants" + ChatColor.GRAY + ": " + ChatColor.YELLOW + getAlivePlayers().size() + ChatColor.GRAY + "/" + ChatColor.RED + getInitialPlayersAmount());
        if (getSpectators().size() != 0) s.add(ChatColor.GOLD + "Spectators" + ChatColor.GRAY + ": " + ChatColor.YELLOW + getSpectators().size());
        return s;
    }

    @Override
    public void setSpectatorsSpawn(Location location) {
         spectatorsSpawn = location;
    }

    @EventHandler
    public void onParticipantDamaged (EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (isAlive(player) && event.getCause() == EntityDamageEvent.DamageCause.FALL && player.getHealth() - event.getFinalDamage() <= 0.0D) {
                removePlayer(player, getPrefix() + ChatColor.RED + "You have not known how to land and you have died.", false, true);
            }
        }
    }

    @EventHandler
    public void onParticipantDamagedByParticipand (EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player player = (Player) event.getEntity(), damager = (Player) event.getDamager();
            if (!isAlive(player) && !isAlive(damager)) {
                return;
            }
            if (player.getHealth() - event.getFinalDamage() <= 0.0D) {
                event.setCancelled(true);
                removePlayer(player, getPrefix() + ChatColor.RED + "You have been killed by " + damager.getDisplayName() + ChatColor.RED + ".", false, true);
                damager.sendMessage(getPrefix() + ChatColor.YELLOW + "You killed " + player.getDisplayName() + ChatColor.YELLOW + ".");
                broadcast(getPrefix() + damager.getDisplayName() +  ChatColor.YELLOW + " has killed " + player.getDisplayName() + ChatColor.YELLOW + ".");
                damager.playSound(damager.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            }
        }
    }
}
