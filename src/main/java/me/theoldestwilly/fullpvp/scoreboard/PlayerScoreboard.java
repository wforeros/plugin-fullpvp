package me.theoldestwilly.fullpvp.scoreboard;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.clan.Clan;
import me.theoldestwilly.fullpvp.event.hosted.structure.HostedEvent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Collections;
import java.util.List;

public final class PlayerScoreboard {

    public static final String[] ENTRY_NAMES = new String[15];

    @Getter
    private final Player player;

    @Setter
    private final ScoreboardProvider provider;

    @Getter
    private final Scoreboard scoreboard;

    private final Objective objective;

    private final Team self;
    private final Team enemy;
    private final Team event;

    @Getter
    private boolean sidebarVisible = true;

    public PlayerScoreboard(Player player, ScoreboardProvider provider) {
        this.player = Preconditions.checkNotNull(player, "Player can not be null");
        this.provider = Preconditions.checkNotNull(provider, "Scoreboard Provider can not be null");

        scoreboard = player.getScoreboard() == Bukkit.getScoreboardManager().getMainScoreboard() ? Bukkit.getScoreboardManager().getNewScoreboard() : player.getScoreboard();

        Objective objective = scoreboard.getObjective("PlayerScoreboard");
        if (objective == null) {
            objective = scoreboard.registerNewObjective("PlayerScoreboard", "dummy");
        }
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(provider.getTitle());

        this.objective = objective;

        Team self = scoreboard.getTeam("self");
        if (self == null) {
            self = scoreboard.registerNewTeam("self");
        }
        self.setPrefix(String.valueOf(ChatColor.DARK_GREEN));

        this.self = self;

        Team other = scoreboard.getTeam("other");
        if (other == null) {
            other = scoreboard.registerNewTeam("other");
        }
        other.setPrefix(String.valueOf(ChatColor.RED));
        this.enemy = other;

        Team event = scoreboard.getTeam("event");
        if (event == null) {
            other = scoreboard.registerNewTeam("event");
        }
        other.setPrefix(ChatColor.AQUA.toString());
        this.event = other;
        player.setScoreboard(scoreboard);
    }

    public boolean isActive() {
        return player != null && player.isOnline() && player.getScoreboard() == scoreboard && sidebarVisible;
    }

    public void updateNametags(Player target) {
        updateNametags(Collections.singleton(target));
    }

    public void updateNametags(Iterable<? extends Player> targets) {
        new BukkitRunnable() {
            //HostedEvent hEvent = FullPvP.getInstance().getHostedEventsManager().getCurrentEvent();
            Clan clan = FullPvP.getInstance().getClanManager().getPlayersClan(player);
            @Override
            public void run() {
                for (Player target : targets) {
                    boolean isTheSamePlayer = target.getUniqueId().equals(player.getUniqueId());
                    if (isTheSamePlayer && !self.hasPlayer(target)) self.addPlayer(target);
                    /*if (hEvent != null && hEvent.isParticipant(target)) {
                        if (!event.hasPlayer(target)) event.addPlayer(target);
                    } else */
                    if (clan != null) {
                        if (event.hasPlayer(target)) event.removePlayer(target);
                        boolean isInSelfTeam = self.hasPlayer(target), isInEnemyTeam = enemy.hasPlayer(target);
                        if (clan.isMember(target.getUniqueId(), target.getName())) {
                            if (isInEnemyTeam) enemy.removePlayer(target);
                            if (!isInSelfTeam) self.addPlayer(target);
                        } else {
                            if (!isTheSamePlayer && isInSelfTeam) self.removePlayer(target);
                            if (!enemy.hasPlayer(target) && !isTheSamePlayer) enemy.addPlayer(target);
                        }
                    } else if (!enemy.hasPlayer(target) && !isTheSamePlayer) enemy.addPlayer(target);
                }
            }
        }.runTaskAsynchronously(FullPvP.getInstance());
    }

    public void update() {
        if (!isActive()) return;

        List<String> lines = provider.getLines(player);

        cleanupOld:
        {
            if (scoreboard.getEntries().size() != lines.size()) {
                scoreboard.getEntries().forEach(this::removeEntry);
            }
            break cleanupOld;
        }

        sendNew:
        {
            int index = 0;
            for (String line : lines) {
                Entry entry = split(line);

                Team team = scoreboard.getTeam(ENTRY_NAMES[index]);
                if (team == null) {
                    try {
                        team = scoreboard.registerNewTeam(ENTRY_NAMES[index]);
                    } catch (IllegalArgumentException ignored) {
                    }
                    team.addEntry(team.getName());
                }
                team.setPrefix(entry.left);
                team.setSuffix(entry.right);

                objective.getScore(team.getName()).setScore(15 - index);

                index++;
            }
            break sendNew;
        }
    }

    private Entry split(String text) {
        Entry entry = new Entry();
        if (text.length() <= 16) {
            entry.left = text;
        } else {
            String prefix = text.substring(0, 16), suffix = "";
            if (prefix.endsWith("\u00a7")) {
                prefix = prefix.substring(0, prefix.length() - 1);
                suffix = "\u00a7" + suffix;
            }
            suffix = StringUtils.left(ChatColor.getLastColors(prefix) + suffix + text.substring(16), 16);
            entry.left = prefix;
            entry.right = suffix;
        }
        return entry;
    }

    private void removeEntry(String id) {
        removeEntry(id, false);
    }

    private void removeEntry(String id, boolean deleteTeam) {
        scoreboard.resetScores(id);
        if (deleteTeam) {
            Team team = scoreboard.getTeam(id);
            if (team != null) team.unregister();
        }
    }

    public void unregister() {
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }

    public void setSidebarVisible(boolean sidebarVisible) {
        this.sidebarVisible = sidebarVisible;
        objective.setDisplaySlot(sidebarVisible ? DisplaySlot.SIDEBAR : null);
    }

    private class Entry {
        private String left = "", right = "";
    }

    static {
        for (int i = 0; i < 15; i++) {
            ENTRY_NAMES[i] = ChatColor.AQUA + ChatColor.values()[i].toString() + ChatColor.RESET;
        }
    }
}