package me.theoldestwilly.fullpvp.staffmode;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.PermissionsManager;
import me.theoldestwilly.fullpvp.users.Profile;
import me.theoldestwilly.fullpvp.utilities.chat.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Iterator;

public class FreezeHandler implements Listener {
    private final FullPvP plugin;

    public FreezeHandler(FullPvP plugin) {
        this.plugin = plugin;
    }

    public boolean isFrozen(Player player) {
        Profile profile = plugin.getProfileHandler().getProfileByUniqueID(player.getUniqueId());
        if (profile != null) return profile.isFrozen();
        return false;
    }


    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.HIGH
    )
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Profile profile = plugin.getProfileHandler().getProfileByUniqueID(player.getUniqueId());
        if (profile != null && profile.isFrozen()) {
            Iterator var3 = Bukkit.getOnlinePlayers().iterator();

            while (var3.hasNext()) {
                Player players = (Player) var3.next();
                if (PermissionsManager.hasStaffPermission(players)) {
                    players.sendMessage(ChatColor.GOLD + player.getName() + " " + ChatColor.RED + "has logged in while is frozen.");
                }
            }
        }

    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.HIGH
    )
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (this.isFrozen(player)) {
            Iterator var3 = Bukkit.getOnlinePlayers().iterator();

            while (var3.hasNext()) {
                Player players = (Player) var3.next();
                if (PermissionsManager.hasStaffPermission(players)) {
                    players.sendMessage(ChatColor.GOLD + player.getName() + " " + ChatColor.RED + "has logged out while is frozen.");
                }
            }
        }

    }


    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.HIGH
    )
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (this.isFrozen(player)) {
                event.setCancelled(true);
            }
        }

    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.HIGH
    )
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Player damaged;
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            damaged = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();
            if (damaged != damager) {
                if (this.isFrozen(damager)) {
                    event.setCancelled(true);
                    damager.sendMessage(TextUtils.formatColor("&cYou can not attack players while you're still frozen."));
                } else if (this.isFrozen(damaged)) {
                    event.setCancelled(true);
                    damager.sendMessage(TextUtils.formatColor("&c" + damaged.getName() + " is currently frozen and can not be damaged."));
                }
            }
        } else if (event.getEntity() instanceof LivingEntity && event.getDamager() instanceof Player) {
            damaged = (Player) event.getDamager();
            if (event.getEntity() != damaged && this.isFrozen(damaged)) {
                event.setCancelled(true);
            }
        }

    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.HIGH
    )
    public void onPotionSplash(PotionSplashEvent event) {
        if (event.getEntity().getShooter() instanceof Player && event.getEntity() instanceof ThrownPotion) {
            Iterator var2 = event.getAffectedEntities().iterator();

            while (var2.hasNext()) {
                Entity entity = (Entity) var2.next();
                if (entity instanceof Player) {
                    Player damager = (Player) event.getEntity().getShooter();
                    Player damaged = (Player) entity;
                    if (this.isFrozen(damaged)) {
                        event.getAffectedEntities().remove(damaged);
                        damager.sendMessage(TextUtils.formatColor("&c" + damaged.getName() + " is currently frozen and can not be damaged."));
                    }
                }
            }
        }

    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.HIGH
    )
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (!event.getMessage().equalsIgnoreCase("/helpop") && this.isFrozen(player)) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You can not execute completers while frozen.");
        }

    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.HIGH
    )
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            Player player = (Player) event.getEntity().getShooter();
            if (this.isFrozen(player)) {
                event.setCancelled(true);
                player.sendMessage(TextUtils.formatColor("&cYou can not interact with that item while you're still frozen."));
            }
        }

    }
}