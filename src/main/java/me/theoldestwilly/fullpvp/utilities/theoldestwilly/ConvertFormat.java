package me.theoldestwilly.fullpvp.utilities.theoldestwilly;


import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class ConvertFormat {
    public ConvertFormat() {
    }

    public static Long convertDelay2Long2(String[] arguments, Player player) {
        Long days2Long = 0L;
        Long hours2Long = 0L;
        Long minutes2Long = 0L;
        Long secs2Long = 0L;
        Long totalLong = 0L;
        String[] var7 = arguments;
        int var8 = arguments.length;

        for(int var9 = 0; var9 < var8; ++var9) {
            String string = var7[var9];
            if (Checkers.containsDifferentLetter(string, Arrays.asList('d', 'h', 'm', 's'), 1)) {
                player.sendMessage(ChatColor.RED + "Invalid time format.");
                return null;
            }

            Integer seconds;
            if (string.contains("d")) {
                seconds = Integer.parseInt(string.replace("d", ""));
                minutes2Long = TimeUnit.DAYS.toMillis((long)seconds);
            }

            if (string.contains("h")) {
                seconds = Integer.parseInt(string.replace("h", ""));
                days2Long = TimeUnit.HOURS.toMillis((long)seconds);
            }

            if (string.contains("m")) {
                seconds = Integer.parseInt(string.replace("m", ""));
                days2Long = TimeUnit.MINUTES.toMillis((long)seconds);
            }

            if (string.contains("s")) {
                seconds = Integer.parseInt(string.replace("s", ""));
                days2Long = TimeUnit.SECONDS.toMillis((long)seconds);
            }
        }

        totalLong = days2Long + hours2Long + minutes2Long + secs2Long;
        return totalLong;
    }

    public static Long convertDelay2Long2(String argument, CommandSender sender) {
        String[] arguments = argument.split(" ");
        Long days2Long = 0L;
        Long hours2Long = 0L;
        Long minutes2Long = 0L;
        Long secs2Long = 0L;
        Long totalLong = 0L;
        String[] var8 = arguments;
        int var9 = arguments.length;

        for(int var10 = 0; var10 < var9; ++var10) {
            String string = var8[var10];
            if (Checkers.containsDifferentLetter(string, Arrays.asList('d', 'h', 'm', 's'), 1)) {
                sender.sendMessage(ChatColor.RED + "Invalid time format.");
                return null;
            }

            Integer seconds;
            if (string.contains("d")) {
                seconds = Integer.parseInt(string.replace("d", ""));
                minutes2Long = TimeUnit.DAYS.toMillis((long)seconds);
            }

            if (string.contains("h")) {
                seconds = Integer.parseInt(string.replace("h", ""));
                days2Long = TimeUnit.HOURS.toMillis((long)seconds);
            }

            if (string.contains("m")) {
                seconds = Integer.parseInt(string.replace("m", ""));
                days2Long = TimeUnit.MINUTES.toMillis((long)seconds);
            }

            if (string.contains("s")) {
                seconds = Integer.parseInt(string.replace("s", ""));
                days2Long = TimeUnit.SECONDS.toMillis((long)seconds);
            }
        }

        totalLong = days2Long + hours2Long + minutes2Long + secs2Long;
        return totalLong;
    }

    public static String setFormat(Long value) {
        return value < TimeUnit.MINUTES.toMillis(1L) ? (new DecimalFormat("0.0")).format((double)value / 1000.0D) + "s" : DurationFormatUtils.formatDuration(value, (value >= TimeUnit.HOURS.toMillis(1L) ? "HH:" : "") + "mm:ss");
    }
}
