package me.theoldestwilly.fullpvp.event.scheduler;

import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

public class CheckCurrentTimeCommand extends ArgumentExecutor {
    public CheckCurrentTimeCommand() {
        super("currenttimeinfo", new String[]{});
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        LocalDateTime now = LocalDateTime.now();
        sender.sendMessage(ChatColor.GOLD  + "Current date info" + ChatColor.GRAY + ": " +
                ChatColor.YELLOW + " Day: " + ChatColor.GRAY + now.getDayOfWeek().getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()) +
                ChatColor.YELLOW + ". Hour: " + ChatColor.GRAY + now.getHour() + ":" + now.getMinute() +
                ":" + now.getSecond());
        return true;
    }
}
