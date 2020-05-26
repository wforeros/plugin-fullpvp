package me.theoldestwilly.fullpvp.scoreboard;

import net.silexpvp.nightmare.util.BukkitUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public interface ScoreboardProvider {

    String STRAIGHT_SCOREBOARD_LINE = BukkitUtils.STRAIGHT_LINE_TEMPLATE.substring(0, 32);

    default String getTitle() {
        return ChatColor.GOLD.toString() + ChatColor.BOLD + "Silex " + ChatColor.RED + "[FullPvP]";
    }

    ;

    List<String> getLines(Player player);
}