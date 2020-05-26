package me.theoldestwilly.fullpvp.scoreboard.provider;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.combatlogger.CombatLoggerHandler;
import me.theoldestwilly.fullpvp.event.global.GlobalEvent;
import me.theoldestwilly.fullpvp.event.hosted.structure.HostedEvent;
import me.theoldestwilly.fullpvp.modes.ServerMode;
import me.theoldestwilly.fullpvp.scoreboard.ScoreboardProvider;
import me.theoldestwilly.fullpvp.users.Profile;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.PluginError;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class FullProvider implements ScoreboardProvider {

    private final FullPvP plugin;
    private String separator = ChatColor.GRAY + ": " + ChatColor.RED;

    public FullProvider(FullPvP plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> lines = new ArrayList<>();
        Profile profile = plugin.getProfileHandler().getProfileByUniqueID(player.getUniqueId());
        if (profile != null) {
            if (profile.hasPvpTimer()) lines.add(ChatColor.GOLD + "PvP Timer" + ChatColor.GRAY + ": " + ChatColor.YELLOW + profile.getDisplayPvpTimer());
            lines.add(ChatColor.GOLD + "Kills" + separator + profile.getKills());
            lines.add(ChatColor.GOLD + "Balance" + separator + ChatColor.GREEN + "$" + profile.getDisplayBalance());

            HostedEvent hostedEvent = plugin.getHostedEventsManager().getCurrentEvent();
            if (hostedEvent != null && hostedEvent.isParticipant(player)) {
                lines.add(ChatColor.GRAY + STRAIGHT_SCOREBOARD_LINE);
                lines.addAll(hostedEvent.getScoreboardLines());
            } else {
                ServerMode mode = plugin.getModesManager().getCurrentMode();
                if (mode != null) {
                    lines.addAll(mode.getScoreboardLines());
                } else {
                    GlobalEvent event = this.plugin.getGlobalEventsManager().getGlobalEvent();
                    if (event != null) {
                        lines.add(ChatColor.GRAY + STRAIGHT_SCOREBOARD_LINE);
                        lines.addAll(event.getScoreboardLines());
                    }
                }
            }
            CombatLoggerHandler handler = plugin.getCombatLoggerHandler();
            Long remaining = handler.getRemainingTag(player);
            if (remaining != null) {
                lines.add(ChatColor.RED.toString() + ChatColor.BOLD + "Combat" + separator + (remaining != null ? TimeUnit.MILLISECONDS.toSeconds(remaining) : 0) + "s");
            }

            if (profile.isInStaffMode()) {
                lines.add(ChatColor.GOLD + "Staff Mode" + separator);
                lines.add(ChatColor.GRAY + " » " + ChatColor.YELLOW + "Vanished" + separator + (profile.isVanished() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"));
                lines.add(ChatColor.GRAY + " » " + ChatColor.YELLOW + "Chat" + separator + (plugin.getNightmare().getProfileManager().getLoadedProfile(player.getUniqueId()).isInStaffChat() ? ChatColor.GREEN + "Staff" : ChatColor.RED + "Global"));
            }
        } else {
            lines.add(ChatColor.RED + "Error number #0" + PluginError.ERROR_LOADING_PROFILE.getErrorNumber());
            lines.add(ChatColor.YELLOW + "Report it!");
        }

        if (!lines.isEmpty()) {
            lines.add(0, ChatColor.GRAY + STRAIGHT_SCOREBOARD_LINE);
            lines.add(lines.size(), ChatColor.GRAY + STRAIGHT_SCOREBOARD_LINE);
        }

        return lines;
    }
}