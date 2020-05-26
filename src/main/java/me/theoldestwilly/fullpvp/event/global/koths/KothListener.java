package me.theoldestwilly.fullpvp.event.global.koths;

import java.util.UUID;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.event.global.koths.classes.KothCapzone;
import me.theoldestwilly.fullpvp.event.global.koths.classes.KothEvent;
import me.theoldestwilly.fullpvp.utilities.cuboid.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class KothListener implements Listener {
    private FullPvP plugin;
    private KothManager manager;
    private KothInteractManager interactManager;

    public KothListener(FullPvP plugin) {
        this.plugin = plugin;
        manager = plugin.getGlobalEventsManager().getKothManager();
        interactManager = manager.getKothInteractManager();
    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (this.plugin.getGlobalEventsManager().getKothManager().isKothActive()) {
            KothEvent koth = manager.getActiveKoth();
            KothCapzone capzone = koth.getKothCapzone();
            Cuboid cuboid = capzone.getProtectedArea().getArea();
            if (koth != null) {
                UUID uuid = koth.getKothCapzone().getCaper();
                if (uuid != null && uuid.equals(event.getPlayer().getUniqueId()) && cuboid.contains(event.getFrom()) && !cuboid.contains(event.getTo())) {
                    this.interactManager.onPlayerCapzoneIteract(event.getPlayer(), koth, true);
                } else if (!cuboid.contains(event.getFrom()) && cuboid.contains(event.getTo()) && uuid == null) {
                    this.interactManager.onPlayerCapzoneIteract(event.getPlayer(), koth, false);
                }
            }
        }

    }

    @EventHandler(
            priority = EventPriority.HIGH
    )
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        int toX = to.getBlockX();
        int toY = to.getBlockY();
        int toZ = to.getBlockZ();
        if (from.getX() != (double)toX || from.getY() != (double)toY || from.getZ() != (double)toZ) {
            this.interactManager.checkPlayerAtKoth(event.getPlayer(), event.getFrom(), event.getTo());
        }

    }

    @EventHandler(
            priority = EventPriority.HIGH
    )
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (this.manager.isKothActive() && this.manager.getActiveKoth().getKothCapzone().getCaper() != null && this.manager.getActiveKoth().getKothCapzone().getCaper().equals(event.getPlayer().getUniqueId())) {
            this.interactManager.onPlayerCapzoneIteract(event.getPlayer(), this.manager.getActiveKoth(), true);
        }

    }

    @EventHandler(
            priority = EventPriority.HIGH
    )
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (this.manager.isKothActive() && this.manager.getActiveKoth().getKothCapzone().getCaper() != null) {
            this.interactManager.onPlayerCapzoneIteract(event.getPlayer(), this.manager.getActiveKoth(), true);
        }

    }

    @EventHandler(
            priority = EventPriority.HIGH
    )
    public void onPlayerDeath(PlayerDeathEvent event) {
        KothEvent kothEvent = this.manager.getActiveKoth();
        if (kothEvent != null && kothEvent.getKothCapzone().getCaper() == event.getEntity().getUniqueId()) {
            this.interactManager.onPlayerCapzoneIteract(event.getEntity(), kothEvent, true);
        }

    }
}
