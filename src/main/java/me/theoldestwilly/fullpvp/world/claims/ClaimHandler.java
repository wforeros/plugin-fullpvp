package me.theoldestwilly.fullpvp.world.claims;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sk89q.worldedit.bukkit.selections.Selection;
import lombok.Getter;
import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.PermissionsManager;
import me.theoldestwilly.fullpvp.utilities.Config;
import me.theoldestwilly.fullpvp.utilities.cuboid.Cuboid;
import me.theoldestwilly.fullpvp.world.serverspawn.ServerSpawn;
import net.silexpvp.nightmare.util.GenericUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class ClaimHandler implements Listener {
    private FullPvP plugin;
    private @Getter Config config;
    private String alias = "claims";
    private Map<String, ClaimedLand> claimsList = new HashMap();

    public ClaimHandler(FullPvP plugin) {
        this.plugin = plugin;
        ConfigurationSerialization.registerClass(ClaimedLand.class);
        ConfigurationSerialization.registerClass(ServerSpawn.class);
        this.config = new Config(plugin, alias);
        loadClaims();
    }

    public void loadClaims() {
        Object object = this.config.get(alias);
        if (object != null && object instanceof List) {
            GenericUtils.createList(object, ClaimedLand.class).forEach(claim -> {
                claimsList.put(claim.getName(), claim);
            });
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + ChatColor.YELLOW + claimsList.size() + " MapChests successfully loaded.");
        }
    }

    public void saveClaims() {
        this.config.set(alias, this.claimsList.values().toArray(new ClaimedLand[0]));
        this.config.save();
    }

    public void onClaimLand(Player player, String name) {
        Selection selection = this.plugin.getWorldEditPlugin().getSelection(player);
        ClaimedLand claimedLand = getByName(name.toLowerCase());
        if (claimedLand != null) {
            player.sendMessage(Lang.ERROR_GLOBAL_DETECTED_OBJECT_WITH_THAT_NAME);
            return;
        }
        if (selection == null) {
            player.sendMessage(ChatColor.RED + "Selection not found.");
        } else {
            Cuboid cuboid = new Cuboid(selection.getMinimumPoint(), selection.getMaximumPoint());
            claimedLand = new ClaimedLand(name, cuboid);
            this.getClaimsList().put(claimedLand.getName(), claimedLand);
            player.sendMessage(ChatColor.GREEN + "You have created the claim: " + ChatColor.YELLOW + name + ChatColor.GREEN + ".");
        }
    }

    public ClaimedLand getByName(String name) {
        Iterator var2 = this.getClaimsList().keySet().iterator();
        String string;
        do {
            if (!var2.hasNext()) {
                return null;
            }
            string = (String)var2.next();
        } while(!string.equalsIgnoreCase(name));
        return this.getClaimsList().get(string);
    }

    public ClaimedLand getByLocation(Location location) {
        Iterator var2 = this.getClaimsList().values().iterator();

        ClaimedLand claimedLand;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            claimedLand = (ClaimedLand)var2.next();
        } while(!claimedLand.getProtectedArea().getArea().contains(location));

        return claimedLand;
    }

    public void checkPlayerAtClaim(Player player, Location from, Location to) {
        Iterator var4 = this.getClaimsList().values().iterator();

        while(var4.hasNext()) {
            ClaimedLand claimedLand = (ClaimedLand)var4.next();
            Cuboid cuboid = claimedLand.getProtectedArea().getArea();
            if (claimedLand.isBroadcastAllowed()) {
                if (cuboid.contains(to) && !cuboid.contains(from)) {
                    player.sendMessage(ChatColor.YELLOW + "Now entering to " + ChatColor.GREEN + claimedLand.getName() + ChatColor.YELLOW + " zone (" + (claimedLand.isPvpAllowed() ? ChatColor.RED + "PvP Enabled" : ChatColor.GREEN + "PvP Disabled") + ChatColor.YELLOW + ").");
                    if (!claimedLand.isFlyAllowed() && !PermissionsManager.hasStaffPermission(player) && player.isFlying()) {
                        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "fly " + player.getName());
                        player.sendMessage(ChatColor.RED + "In this territory the use of fly is not allowed, therefore it have been deactivated!");
                    }
                    break;
                }

                if (cuboid.contains(from) && !cuboid.contains(to)) {
                    player.sendMessage(ChatColor.YELLOW + "Now leaving " + ChatColor.RED + claimedLand.getName() + ChatColor.YELLOW + " zone.");
                    if (!PermissionsManager.hasStaffPermission(player) && !this.isAllowFlyInLocation(to) && PermissionsManager.hasDonatorPermission(player)) {
                        if (player.isFlying()) {
                            player.setFlying(false);
                            player.setAllowFlight(false);
                            player.sendMessage(ChatColor.RED + "In this territory the use of fly is not allowed, therefore it have been deactivated!");
                        }

                        player.setFlying(false);
                        player.setAllowFlight(false);
                    }
                    break;
                }

                if ((to.getBlockX() != from.getBlockX() || to.getBlockZ() != from.getBlockZ()) && cuboid.contains(to.getBlock()) && !player.hasPermission(claimedLand.getPermission())) {
                    player.teleport(from);
                    player.sendMessage(ChatColor.RED + "You are not allowed to join to this claim.");
                }
            }
        }

    }

    public Boolean isAllowFlyInLocation(Location location) {
        Iterator var2 = this.getClaimsList().values().iterator();

        ClaimedLand claimedLand;
        Cuboid cuboid;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            claimedLand = (ClaimedLand)var2.next();
            cuboid = claimedLand.getProtectedArea().getArea();
        } while(!cuboid.contains(location));

        return claimedLand.isFlyAllowed();
    }

    /**
     *
     * Method used to check if a player clan attack into his/her location
     * @param damager Damager
     * @return
     */
    public boolean isAttackPossible(Player damager) {
        for (ClaimedLand claim : getClaimsList().values()) {
            if (claim.getProtectedArea().getArea().contains(damager)) {
                return claim.isPvpAllowed();
            }
        }
        return true;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        int toX = to.getBlockX();
        int toY = to.getBlockY();
        int toZ = to.getBlockZ();
        if (from.getX() != (double)toX || from.getY() != (double)toY || from.getZ() != (double)toZ) {
            this.checkPlayerAtClaim(event.getPlayer(), event.getFrom(), event.getTo());
        }

    }

    @EventHandler(
            priority = EventPriority.LOWEST
    )
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().contains("/fly")) {
            if (!PermissionsManager.hasStaffPermission(event.getPlayer())) {
                Player player = event.getPlayer();
                if (!player.isFlying() && !this.isAllowFlyInLocation(player.getLocation())) {
                    player.sendMessage(ChatColor.RED + "Fly command is not allowed in this zone.");
                    event.setCancelled(true);
                }
            }
        }

    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        TeleportCause tpCause = event.getCause();
        if (player.getOpenInventory() != null || !player.isOnGround()) {
            player.closeInventory();
        }

        Iterator var4 = this.getClaimsList().values().iterator();

        while(true) {
            ClaimedLand claimedLand;
            Cuboid cuboid;
            do {
                if (!var4.hasNext()) {
                    return;
                }

                claimedLand = (ClaimedLand)var4.next();
                cuboid = claimedLand.getProtectedArea().getArea();
            } while(!cuboid.contains(event.getTo()));

            if (tpCause == TeleportCause.ENDER_PEARL && !claimedLand.isPearlAllowed()) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "Ender pearls are not allowed in that zone.");
            } else if (tpCause == TeleportCause.COMMAND) {
                label55: {
                    if (!player.hasPermission(claimedLand.getPermission())) {
                        if (!PermissionsManager.hasStaffPermission(player)) {
                            event.setCancelled(true);
                            player.sendMessage(ChatColor.RED + "You are not allowed to join to this claim.");
                            break label55;
                        }
                    }

                    if (!claimedLand.isPvpAllowed()) {
                        //this.plugin.getTagsManager().removeTagg(player.getUniqueId());
                    }
                }
            }

            if (!claimedLand.isFlyAllowed() && player.getAllowFlight()) {
                if (!PermissionsManager.hasStaffPermission(player)) {
                    player.setFlying(false);
                    player.setAllowFlight(false);
                    player.sendMessage(ChatColor.RED + "Fly is not allowed into this claim.");
                }
            }
        }
    }

    @EventHandler(
            priority = EventPriority.LOWEST,
            ignoreCancelled = true
    )
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ClaimedLand claimedLand = this.getByLocation(player.getLocation());
        if (claimedLand != null && !player.hasPermission(claimedLand.getPermission())) {
            if (!PermissionsManager.hasStaffPermission(player)) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You can not use that.");
            }
        }

    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.LOWEST
    )
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Iterator var2 = getClaimsList().values().iterator();

            while(var2.hasNext()) {
                ClaimedLand claimedLand = (ClaimedLand)var2.next();
                if (claimedLand.getProtectedArea().getArea().contains(event.getDamager().getLocation())) {
                    if (!claimedLand.isPvpAllowed()) {
                        event.setCancelled(true);
                        (event.getDamager()).sendMessage(Lang.ERROR_CLAIMS_PVP_NOT_ALLOWED);
                    }
                    break;
                }
            }
        } else {
            Iterator var4;
            ClaimedLand claimedLand;
            Player damager;
            if (event.getDamager() instanceof FishHook) {
                FishHook fishHook = (FishHook)event.getDamager();
                damager = (Player)fishHook.getShooter();
                var4 = getClaimsList().values().iterator();

                while(var4.hasNext()) {
                    claimedLand = (ClaimedLand)var4.next();
                    if (claimedLand.getProtectedArea().getArea().contains(damager.getLocation())) {
                        if (!claimedLand.isPvpAllowed()) {
                            event.setCancelled(true);
                            damager.sendMessage(ChatColor.RED + "PvP is not allowed here.");
                        }
                        break;
                    }
                }
            } else if (event.getDamager() instanceof Projectile) {
                Projectile snowball = (Projectile)event.getDamager();
                damager = (Player)snowball.getShooter();
                var4 = getClaimsList().values().iterator();

                while(var4.hasNext()) {
                    claimedLand = (ClaimedLand)var4.next();
                    if (claimedLand.getProtectedArea().getArea().contains(damager.getLocation())) {
                        if (!claimedLand.isPvpAllowed()) {
                            event.setCancelled(true);
                            damager.sendMessage(ChatColor.RED + "PvP is not allowed here.");
                        }
                        break;
                    }
                }
            }
        }

    }

    @EventHandler(
            ignoreCancelled = false,
            priority = EventPriority.LOWEST
    )
    public void onDamage(EntityDamageEvent event) {
        Iterator var2 = getClaimsList().values().iterator();

        while(var2.hasNext()) {
            ClaimedLand claimedLand = (ClaimedLand)var2.next();
            if (claimedLand.getProtectedArea().getArea().contains(event.getEntity().getLocation())) {
                if (!claimedLand.isPvpAllowed()) {
                    event.setCancelled(true);
                }
                break;
            }
        }

    }

    @EventHandler
    public void onPlayerHitFishingRodEventThingyName(PlayerFishEvent event) {
        Player player = event.getPlayer();
        if (event.getCaught() instanceof Player) {
            Player caught = (Player)event.getCaught();
            if (player.getItemInHand().getType() == Material.FISHING_ROD) {
                Iterator var4 = getClaimsList().values().iterator();

                while(var4.hasNext()) {
                    ClaimedLand claimedLand = (ClaimedLand)var4.next();
                    if (claimedLand.getProtectedArea().getArea().contains(player.getLocation())) {
                        if (!claimedLand.isPvpAllowed()) {
                            event.setCancelled(true);
                            player.sendMessage(ChatColor.RED + "PvP is not allowed here.");
                        }
                        break;
                    }
                }
            }
        }

    }

    @EventHandler(
            ignoreCancelled = false,
            priority = EventPriority.LOWEST
    )
    public void onFoodChange(FoodLevelChangeEvent event) {
        Iterator var2 = getClaimsList().values().iterator();

        while(var2.hasNext()) {
            ClaimedLand claimedLand = (ClaimedLand)var2.next();
            if (claimedLand.getProtectedArea().getArea().contains(event.getEntity().getLocation())) {
                if (!claimedLand.isPvpAllowed()) {
                    event.setFoodLevel(20);
                }
                break;
            }
        }

    }

    public Map<String, ClaimedLand> getClaimsList() {
        return this.claimsList;
    }
}
