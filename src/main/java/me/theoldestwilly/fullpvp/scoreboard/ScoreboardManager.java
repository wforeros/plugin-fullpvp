package me.theoldestwilly.fullpvp.scoreboard;

import lombok.Getter;
import lombok.Setter;
import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.clan.events.*;
import me.theoldestwilly.fullpvp.scoreboard.provider.FullProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class ScoreboardManager implements Listener {

    private final FullPvP plugin;

    //The clan tag update is into Clan class
    @Getter @Setter private final ScoreboardProvider provider;

    private Map<UUID, PlayerScoreboard> scoreboards = new ConcurrentHashMap<>();

    private BukkitTask task;

    public ScoreboardManager(FullPvP plugin) {
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);

        provider = new FullProvider(plugin);

        Bukkit.getOnlinePlayers().forEach(this::getOrCreateScoreboard);

        task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::updateAll, 2L, 2L);
    }

    public PlayerScoreboard getPlayerScoreboard(Player player) {
        return scoreboards.get(player.getUniqueId());
    }

    public PlayerScoreboard getOrCreateScoreboard(Player player) {
        PlayerScoreboard scoreboard = new PlayerScoreboard(player, provider);
        PlayerScoreboard current = scoreboards.put(player.getUniqueId(), scoreboard);
        if (current != null && current != scoreboard) {
            current.unregister();
        }

        scoreboard.setSidebarVisible(true);
        scoreboard.updateNametags(Bukkit.getOnlinePlayers());
        return scoreboard;
    }

    public void unregisterAll() {
        scoreboards.values().forEach(PlayerScoreboard::unregister);
        scoreboards.clear();

        HandlerList.unregisterAll(this);

        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        getOrCreateScoreboard(player);
        scoreboards.values().forEach(scoreboard -> scoreboard.updateNametags(player));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerScoreboard scoreboard = getPlayerScoreboard(player);
        if (scoreboard != null) {
            scoreboard.unregister();
            scoreboards.remove(player.getUniqueId());
        }
    }

    private void updateAll() {
        scoreboards.values().forEach(PlayerScoreboard::update);
    }
}