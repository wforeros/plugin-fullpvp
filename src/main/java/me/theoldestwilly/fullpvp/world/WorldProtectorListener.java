package me.theoldestwilly.fullpvp.world;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.utilities.chat.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WorldProtectorListener implements Listener {
    FullPvP plugin;
    private boolean isWorldProtector = false;

    public WorldProtectorListener(FullPvP plugin) {
        this.plugin = plugin;
        isWorldProtector = plugin.getConfig().getBoolean("enable-world-protector");
    }

    @EventHandler(
            priority = EventPriority.HIGH
    )
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (isWorldProtector && player.getGameMode() != GameMode.CREATIVE || !player.hasPermission("fullpvp.basicprotector.bypass")) {
            event.setCancelled(true);
            if (!player.isOnGround()) {
                Location location = player.getLocation();
                Block block = location.getBlock();
                if (!block.getType().isSolid()) {
                    player.setVelocity(player.getVelocity().setY(-1.0));
                }
            }
        }

    }

    @EventHandler(
            priority = EventPriority.HIGH
    )
    public void onBreakBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (isWorldProtector && player.getGameMode() != GameMode.CREATIVE || !player.hasPermission("fullpvp.basicprotector.bypass")) {
            event.setCancelled(true);
        }

    }

    @EventHandler(
            priority = EventPriority.HIGH
    )
    public void onMobSpawning(CreatureSpawnEvent event) {
        if (isWorldProtector && event.getEntity().getType() != EntityType.VILLAGER) {
            event.setCancelled(true);
        }

    }

    @EventHandler(
            priority = EventPriority.HIGH
    )
    public void onLavaBucket(PlayerBucketEmptyEvent event) {
        if (isWorldProtector && event.getPlayer().getGameMode() != GameMode.CREATIVE || !event.getPlayer().hasPermission("fullpvp.basicprotector.bypass")) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onExploit(ExplosionPrimeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.HIGH
    )
    public void onPlayerClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.SKULL) {
            BlockState block = event.getClickedBlock().getState();
            Skull skull = (Skull)block;
            String skullOwner = skull.getOwner();
            if (skullOwner != null) {
                Player player = event.getPlayer();
                player.sendMessage(TextUtils.formatColor("&6This head belongs to &e" + skullOwner + "&6."));

            }
        }

    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    private static final void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.SOIL) {
            event.setCancelled(true);
        }

    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    private static final void onEntityInteract(EntityInteractEvent event) {
        if (event.getEntityType() != EntityType.PLAYER && event.getBlock().getType() == Material.SOIL) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (event.toWeatherState()) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onThunderChange(ThunderChangeEvent event) {
        if (event.toThunderState()) {
            event.setCancelled(true);
        }

    }
}

