package me.theoldestwilly.fullpvp.staffmode;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.PermissionsManager;
import me.theoldestwilly.fullpvp.combatlogger.event.TagApplicationEvent;
import me.theoldestwilly.fullpvp.users.Profile;
import me.theoldestwilly.fullpvp.utilities.chat.TextUtils;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.PluginError;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

import java.util.*;

public class StaffModeHandler implements Listener {
    private FullPvP plugin;

    public StaffModeHandler(FullPvP plugin) {
        this.plugin = plugin;
    }

    public void randomTeleport(Player staff) {
        if (Bukkit.getServer().getOnlinePlayers().size() > 1) {
            Random random = new Random();
            Player randomPlayer = staff;
            while (staff == randomPlayer)
                randomPlayer = (Player) Bukkit.getOnlinePlayers().toArray()[random.nextInt(Bukkit.getOnlinePlayers().size())];
            staff.teleport(randomPlayer.getLocation());
            staff.sendMessage(ChatColor.GOLD + "You have been succesfully teleported to " + ChatColor.WHITE + randomPlayer.getName() + ChatColor.GOLD + " location.");
        } else {
            staff.sendMessage(ChatColor.RED + "Insufficient players to use the random teleport tool.");
        }

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!PermissionsManager.hasStaffPermission(player)) {
            for (Profile profile : plugin.getProfileHandler().getOnlinePlayers().values()) {
                if (profile.isInStaffMode()) player.hidePlayer(profile.toPlayer());
            }
        }

    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onStaffQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (PermissionsManager.hasStaffPermission(player)) {
            Profile profile = plugin.getProfileHandler().getProfileByUniqueID(player.getUniqueId());
            profile.setInStaffMode(false, false);
        }
    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.HIGH
    )
    public void onPlaceBlock(BlockPlaceEvent event) {
        if (PermissionsManager.hasStaffPermission(event.getPlayer())) {
            Profile profile = plugin.getProfileHandler().getProfileByUniqueID(event.getPlayer().getUniqueId());
            if (profile.isInStaffMode()) {
                event.setCancelled(true);
            }
        }

    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.HIGH
    )
    public void onDamageEvent(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player) event.getEntity();
            if (PermissionsManager.hasStaffPermission(player)) {
                Profile profile = plugin.getProfileHandler().getProfileByUniqueID(player.getUniqueId());
                if (profile.isInStaffMode()) event.setCancelled(true);
            }
        }
    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.HIGH
    )
    public void onBreakBlock(BlockBreakEvent event) {
        if (PermissionsManager.hasStaffPermission(event.getPlayer())) {
            if (plugin.getProfileHandler().getProfileByUniqueID(event.getPlayer().getUniqueId()).isInStaffMode())
                event.setCancelled(true);
        }

    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.HIGH
    )
    public void onEntityDamageEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            if (PermissionsManager.hasStaffPermission(damager)) {
                if (plugin.getProfileHandler().getProfileByUniqueID(damager.getUniqueId()).isInStaffMode())
                    event.setCancelled(true);
            }
        }

    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.HIGH
    )
    public void onItemDrop(PlayerDropItemEvent event) {
        if (PermissionsManager.hasStaffPermission(event.getPlayer())) {
            if (plugin.getProfileHandler().getProfileByUniqueID(event.getPlayer().getUniqueId()).isInStaffMode())
                event.setCancelled(true);
        }
    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.HIGH
    )
    public void onPickupItem(PlayerPickupItemEvent event) {
        if (PermissionsManager.hasStaffPermission(event.getPlayer())) {
            if (plugin.getProfileHandler().getProfileByUniqueID(event.getPlayer().getUniqueId()).isInStaffMode())
                event.setCancelled(true);
        }
    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.HIGH
    )
    public void onStaffDeath(PlayerDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = event.getEntity();
            if (PermissionsManager.hasStaffPermission(player)) {
                if (plugin.getProfileHandler().getProfileByUniqueID(player.getUniqueId()).isInStaffMode()) {
                    event.setDroppedExp(0);
                    event.getDrops().clear();
                }
            }
        }
    }

    @EventHandler(
            ignoreCancelled = true
    )
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (PermissionsManager.hasStaffPermission(player)) {
            Profile profile = plugin.getProfileHandler().getProfileByUniqueID(player.getUniqueId());
            if (profile.isInStaffMode()) {
                profile.setInStaffMode(false, true);
            }

        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (PermissionsManager.hasStaffPermission(player)) {
                if (plugin.getProfileHandler().getProfileByUniqueID(player.getUniqueId()).isInStaffMode()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (PermissionsManager.hasStaffPermission(player)) {
            if (plugin.getProfileHandler().getProfileByUniqueID(player.getUniqueId()).isInStaffMode()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(
            priority = EventPriority.HIGH
    )
    public void onInteractEvent(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Player) {
            Player player = event.getPlayer();
            if (PermissionsManager.hasStaffPermission(player)) {
                Profile profile = plugin.getProfileHandler().getProfileByUniqueID(player.getUniqueId());
                Player target = (Player) event.getRightClicked();
                if (profile != null) {
                    if (!profile.isInStaffMode()) return;
                    if (player.getItemInHand().isSimilar(StaffMode.INSPECTION_TOOL.getItem())) {
                        player.performCommand("invsee " + target.getName());
                    } else if (player.getItemInHand().isSimilar(StaffMode.FOLLOW_TOOL.getItem()) && !target.isInsideVehicle() && !player.isInsideVehicle()) {
                        target.setPassenger(player);
                    } else if (player.getItemInHand().isSimilar(StaffMode.FREEZE_TOOL.getItem())) {
                        Profile targetProfile = plugin.getProfileHandler().getProfileByUniqueID(target.getUniqueId());
                        if (targetProfile == null) {
                            player.sendMessage(Lang.ERROR_COMMON_MESSAGE(PluginError.ERROR_LOADING_TARGETS_PROFILE));
                            return;
                        }
                        if (targetProfile.isFrozen()) {
                            targetProfile.setFrozen(false);
                            Command.broadcastCommandMessage(player, TextUtils.formatColor("&f" + target.getName() + " &6is not longer frozen."));
                        } else if (PermissionsManager.hasStaffPermission(target)) {
                            player.sendMessage(TextUtils.formatColor("&c" + target.getName() + " is an Staff Member and can not be frozen."));
                        } else {
                            targetProfile.setFrozen(true);
                            Command.broadcastCommandMessage(player, TextUtils.formatColor("&f" + target.getName() + " &6is now frozen."));
                        }
                    }
                } else player.sendMessage(Lang.ERROR_COMMON_MESSAGE(PluginError.ERROR_LOADING_PROFILE) + ChatColor.GRAY + " (StaffMode Handler)");
            }
        }

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onTagApply(TagApplicationEvent event) {
        if (PermissionsManager.hasStaffPermission(event.getPlayer())) {
            Profile profile = plugin.getProfileHandler().getProfileByUniqueID(event.getPlayer().getUniqueId());
            if (profile != null && profile.isInStaffMode()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(
            priority = EventPriority.LOWEST
    )
    public void onPlayerUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (PermissionsManager.hasStaffPermission(player)) {
            Profile profile = plugin.getProfileHandler().getProfileByUniqueID(player.getUniqueId());
            if (profile != null && profile.isInStaffMode()) {
                int toolID = player.getItemInHand().getDurability();
                Material tool = player.getItemInHand().getType();
                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (toolID != 8 && toolID != 10) {
                        if (tool == Material.RECORD_3) {
                            randomTeleport(player);
                        }
                    } else {
                        profile.updateVanish();
                    }
                }
            }
        }
    }
}
