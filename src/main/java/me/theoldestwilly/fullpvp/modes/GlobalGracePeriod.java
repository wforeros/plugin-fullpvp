package me.theoldestwilly.fullpvp.modes;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.scoreboard.ScoreboardProvider;
import me.theoldestwilly.fullpvp.utilities.JavaUtils;
import org.bukkit.ChatColor;

import java.util.LinkedList;
import java.util.List;

public class GlobalGracePeriod extends ServerMode {

    public GlobalGracePeriod(FullPvP plugin, Long duration) {
        super(plugin, false, duration);
    }

    @Override
    public List<String> getScoreboardLines() {
        List<String> list = new LinkedList<>();
        list.add(ChatColor.GRAY + ScoreboardProvider.STRAIGHT_SCOREBOARD_LINE);
        list.add(ChatColor.GOLD + "Global Grace Period");
        list.add(ChatColor.GREEN + "PvP Disabled");
        list.add(ChatColor.GOLD + "Remaining" + ChatColor.GRAY + ": " + ChatColor.RED + JavaUtils.setFormat(getRemainingTime()));
        if (getRemainingTime() <= 0) finish();
        return list;
    }

}
