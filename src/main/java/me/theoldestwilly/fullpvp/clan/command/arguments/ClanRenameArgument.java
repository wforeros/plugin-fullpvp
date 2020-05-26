package me.theoldestwilly.fullpvp.clan.command.arguments;

import java.util.Collections;
import java.util.List;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClanRenameArgument extends CommandArgument {
    private final FullPvP plugin;

    public ClanRenameArgument(FullPvP plugin) {
        super("rename", "Renames the clan.");
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <clanName>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            if (arguments.length < 2) {
                sender.sendMessage(Lang.SHOW_COMMAND_USAGE(getUsage(label)));
            } else {
                this.plugin.getClanManager().renameClan((Player)sender, arguments[1]);
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You can not execute this command on console.");
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
