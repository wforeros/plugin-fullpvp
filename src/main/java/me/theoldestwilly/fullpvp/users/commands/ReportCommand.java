package me.theoldestwilly.fullpvp.users.commands;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.utilities.chat.TextUtils;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportCommand extends ArgumentExecutor {
    private final FullPvP plugin;

    public ReportCommand(FullPvP plugin) {
        super("executor");
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (arguments.length >= 2) {
                if (Bukkit.getServer().getPlayer(arguments[0]) != null) {
                    if (!arguments[0].equals(player.getName())) {
                        this.plugin.getPlayersCommandsManager().onReportUse(player, Bukkit.getServer().getPlayer(arguments[0]), arguments);
                    } else {
                        player.sendMessage(ChatColor.RED + "You may not report yourself.");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Player " + arguments[0] + " is not online.");
                }
            } else {
                player.sendMessage(TextUtils.formatColor("&cUsage: /" + label + " <player> <reason>"));
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You can not execute this command on console");
        }

        return true;
    }
}