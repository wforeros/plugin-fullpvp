package me.theoldestwilly.fullpvp.combatlogger;

import com.mojang.authlib.GameProfile;
import lombok.Getter;
import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.clan.Clan;
import me.theoldestwilly.fullpvp.combatlogger.event.CombatLoggerDamageByPlayerEvent;
import me.theoldestwilly.fullpvp.combatlogger.event.CombatLoggerDeathEvent;
import me.theoldestwilly.fullpvp.combatlogger.event.CombatLoggerSpawnEvent;
import me.theoldestwilly.fullpvp.combatlogger.event.TagApplicationEvent;
import me.theoldestwilly.fullpvp.utilities.Config;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.GameUtils;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class CombatLoggerHandler implements Listener {
    private List<CombatLogger> combatLoggers = Collections.synchronizedList(new ArrayList<>());
    private FullPvP plugin;
    private Map<UUID, Long> taggedPlayers = new HashMap<>();
    private @Getter List<String> allowedCommands;
    private int tagDuration = 15, loggerDuration = 30;

    public CombatLoggerHandler(FullPvP plugin) {
        this.plugin = plugin;
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, loggersTask(), 20L, 20L);
        Config config = plugin.getConfig();
        tagDuration = config.getInt("tag.duration");
        loggerDuration = config.getInt("tag.logger-duration");
        allowedCommands = config.getStringList("tag.allowed-commands");
        if (allowedCommands == null) allowedCommands = new ArrayList<>();
    }

    private Runnable loggersTask() {
        return  () -> {
            Iterator<CombatLogger> iterator = combatLoggers.iterator();
            while (iterator.hasNext()) {
                CombatLogger logger = iterator.next();
                if (logger.hasLoggerTimeEnded()) {
                    logger.setLocation(logger.getLocation());
                    logger.removeEntity();
                }
            }
        };
    }

    public void removeAllTags() {
        taggedPlayers.clear();
        combatLoggers.stream().forEach(logger -> logger.removeEntity());
        combatLoggers.clear();
    }

    public void removeTag(Player player) {
        taggedPlayers.remove(player.getUniqueId());
    }

    public CombatLogger getCombatLoggerByUniqueId(UUID uniqueId) {
        return combatLoggers.stream().filter(combatLogger -> combatLogger.getPlayerUniqueId().equals(uniqueId)).findFirst().orElse(null);
    }

    public CombatLogger getCombatLoggerByName(String name) {
        String finalName = ChatColor.stripColor(name);
        return combatLoggers.stream().filter(combatLogger -> combatLogger.getPlayerName().equals(finalName)).findFirst().orElse(null);
    }

    public CombatLogger getCombatLoggerByEntity(Entity entity) {
        return combatLoggers.stream().filter(combatLogger -> combatLogger.getEntity().equals(entity)).findFirst().orElse(null);
    }

    public void asyncOnLoggerDeath(CombatLogger logger, Location location) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> onLoggerDeath(logger, location));
    }

    private void onLoggerDeath(CombatLogger logger, Location location) {
        String name = logger.getPlayerName();
        UUID uniqueId = logger.getPlayerUniqueId();
        Player target;
        GameProfile profile = new GameProfile(uniqueId, name);
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        EntityPlayer entity = new EntityPlayer(server, server.getWorldServer(0), profile, new PlayerInteractManager(server.getWorldServer(0)));
        entity.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        entity.world = ((CraftWorld) location.getWorld()).getHandle();
        target = entity == null ? null : entity.getBukkitEntity();
        if (target != null) target.loadData();
        target.getInventory().clear();
        target.getInventory().setArmorContents(null);
        entity.setHealth(0);
        target.saveData();
        plugin.getPlayersHandler().onEntityDeath(logger.getEntity(), logger.getPlayerUniqueId());
        combatLoggers.remove(logger);
    }

    public Long getRemainingTag(Player player) {
        Long time = taggedPlayers.get(player.getUniqueId()),
                remaining = time != null ? (TimeUnit.SECONDS.toMillis(tagDuration) - (System.currentTimeMillis() - taggedPlayers.get(player.getUniqueId()))) : null;
        if (remaining != null && remaining <= 0) taggedPlayers.remove(player.getUniqueId());
        return remaining;
    }

    public boolean isTagged(Player player) {
        Long remaining = getRemainingTag(player);
        return remaining != null && remaining > 0;
    }

    public void applyTag(Player damager, Entity damaged) {
        TagApplicationEvent event = new TagApplicationEvent(damager, damaged);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;
        UUID uniqueId = damager.getUniqueId();
        if (!taggedPlayers.containsKey(uniqueId)) damager.sendMessage(Lang.WARNING_TAG_PLAYER);
        taggedPlayers.put(damager.getUniqueId(), System.currentTimeMillis());

        uniqueId = damaged.getUniqueId();
        if (!taggedPlayers.containsKey(uniqueId)) damaged.sendMessage(Lang.WARNING_TAG_PLAYER);
        taggedPlayers.put(damaged.getUniqueId(), System.currentTimeMillis());

    }

    @EventHandler
    public void onPlayerSpawnEvent(PlayerSpawnLocationEvent event) {
        CombatLogger logger = getCombatLoggerByUniqueId(event.getPlayer().getUniqueId());
        if (logger != null) {
            event.setSpawnLocation(logger.getLocation());
            if (logger.isStillAnEntity()) {
                event.getPlayer().setHealth(logger.getHealth() * 0.5);
                logger.removeEntity();
                combatLoggers.remove(logger);
            }
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (plugin.getClaimHandler().isAttackPossible(event.getPlayer()) && isTagged(player)) {
            CombatLoggerSpawnEvent event1 = new CombatLoggerSpawnEvent(player);
            Bukkit.getPluginManager().callEvent(event1);
            if (!event1.isCancelled()) combatLoggers.add(new CombatLogger(event.getPlayer(), TimeUnit.SECONDS.toMillis(loggerDuration)));
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        ProjectileSource projectileSource = null;
        if (event.getDamager() instanceof Arrow) {
            projectileSource = ((Arrow) event.getDamager()).getShooter();
        }
        if ((projectileSource != null && projectileSource instanceof Player) || event.getDamager() instanceof Player) {
            Entity entity = event.getEntity();
            Player damager = GameUtils.getPlayerDamagerFromEntity(event.getDamager());
            if (event.getEntity() instanceof Villager) {
                CombatLogger logger = getCombatLoggerByName(event.getEntity().getCustomName());
                if (logger == null) return;
                Entity damaged = event.getEntity();
                if (damager == null) return;
                CombatLoggerDamageByPlayerEvent event1 = new CombatLoggerDamageByPlayerEvent(damager, logger);
                Bukkit.getPluginManager().callEvent(event1);
                if (!event1.isCancelled()) {
                    if (!plugin.getClaimHandler().isAttackPossible(damager)) {
                        event.setCancelled(true);
                        damager.sendMessage(Lang.ERROR_CLAIMS_PVP_NOT_ALLOWED);
                    } else {
                        logger.setUnsafeLogoutTime(System.currentTimeMillis());
                        applyTag(damager, damaged);
                    }
                } else {
                    event.setCancelled(true);
                }
            } else if (entity instanceof Player) {
                if (plugin.getClaimHandler().isAttackPossible((damager))) {
                    Player damaged = (Player) entity;
                    applyTag(damager, damaged);
                }
            }
        }
    }

    @EventHandler
    public void onCombatLoggerDamageByPlayer(CombatLoggerDamageByPlayerEvent event) {
        Player damager = event.getDamager();
        Clan clan = plugin.getClanManager().getPlayersClan(damager);
        if (clan != null && clan.isMember(event.getCombatLogger().getPlayerName())) {
            event.setCancelled(true);
            damager.sendMessage(Lang.WARNING_CLAN_CANNOT_ATTACK_MATES);
        }
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Villager) {
            Villager villager = (Villager) event.getEntity();
            Object object = villager.getKiller();
            if (!(object instanceof Player)) return;
            CombatLogger logger = getCombatLoggerByName(villager.getCustomName());
            if (logger == null) return;
            for (ItemStack itemStack : logger.getContents()) {
                event.getDrops().add(itemStack);
            }
            for (ItemStack itemStack : logger.getArmorContents()) {
                event.getDrops().add(itemStack);
            }
            Location location = villager.getLocation();
            CombatLoggerDeathEvent event1 = new CombatLoggerDeathEvent((Player)object, logger);
            Bukkit.getPluginManager().callEvent(event1);
            if (!event1.isCancelled())
                asyncOnLoggerDeath(logger, location);
        }
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onChunkUnloadEvent (ChunkUnloadEvent event) {
        Arrays.stream(event.getChunk().getEntities()).forEach(entity -> {
            CombatLogger logger = getCombatLoggerByEntity(entity);
            if (logger != null) {
                logger.removeEntity();
                combatLoggers.remove(logger);
            }
        });
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (!player.isOp() && isTagged(player) && !allowedCommands.contains(event.getMessage().split(" ")[0])) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Lang.ERROR_TAG_COMMAND_NOT_ALLOWED_WHILE_TAGGED);
        }
    }
}
