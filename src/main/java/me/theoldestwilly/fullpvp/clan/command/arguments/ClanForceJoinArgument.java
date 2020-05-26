package me.theoldestwilly.fullpvp.clan.command.arguments;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ClanForceJoinArgument extends CommandArgument {
    private final FullPvP plugin;

    public ClanForceJoinArgument(FullPvP plugin) {
        super("forcejoin", "Staff: Forcejoin to clan.");
        this.permission = "fullpvp.command.clan.argument.forcejoin";
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
                this.plugin.getClanManager().joinClan((Player) sender, arguments[1], true);
            }
        } else {
            sender.sendMessage(Lang.CONSOLE_NOT_ALLOWED);
        }

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            return Collections.emptyList();
        } else {
            List<String> list = new ArrayList();
            list.addAll(this.plugin.getClanManager().getClansList().keySet());
            return list;
        }
    }
}

