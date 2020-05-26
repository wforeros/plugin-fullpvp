package me.theoldestwilly.fullpvp.users.commands;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class StatsCommand extends ArgumentExecutor {
    private final FullPvP plugin;

    public StatsCommand(FullPvP plugin) {
        super("stats", new String[]{"stat"});
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length == 0) {
            if (sender instanceof Player) plugin.getPlayersCommandsManager().sendStatsMessage(sender, (Player) sender);
            else sender.sendMessage(Lang.SHOW_COMMAND_USAGE('/' + label + " <player>"));
        } else {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(arguments[0]);
            if (offlinePlayer != null) {
                this.plugin.getPlayersCommandsManager().sendStatsMessage(sender, offlinePlayer);
            } else {
                sender.sendMessage(Lang.ERROR_PLAYER_HASNT_PLAYED_BEFORE);
            }
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}

