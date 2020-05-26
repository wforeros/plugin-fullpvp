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

public class ClanCancelInvitationsArgument extends CommandArgument {
    private final FullPvP plugin;

    public ClanCancelInvitationsArgument(FullPvP plugin) {
        super("clearinvited", "Cleans invited players list.", new String[]{"ci"});
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            if (arguments.length < 1) {
                sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            } else {
                this.plugin.getClanManager().deinviteAllPlayers((Player)sender);
            }
        } else {
            sender.sendMessage(Lang.CONSOLE_NOT_ALLOWED);
        }

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 2 ? null : Collections.emptyList();
    }
}
